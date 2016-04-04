package org.littlewings.twitterimages

import java.io.{BufferedInputStream, BufferedOutputStream, IOException}
import java.nio.file.{Files, Paths}
import java.text.SimpleDateFormat

import org.slf4j.LoggerFactory
import twitter4j.{Paging, TwitterFactory}

import scala.collection.JavaConverters._

object TwitterImagesCollector {
  def main(args: Array[String]): Unit = {
    val parser = StartupOption.newParser(getClass)
    val option =
      parser
        .parse(args, StartupOption())
        .getOrElse(sys.exit(1))

    val logger = LoggerFactory.getLogger(getClass)

    val twitter = TwitterFactory.getSingleton

    val outputBaseDir = option.outputDir
    val screenName = option.screenName
    var currentPage = option.pagingStart
    val pagingEnd = option.pagingEnd
    val limit = option.limit
    val imageType = option.imageType
    val formatter = new SimpleDateFormat("yyyyMMddHHmmss")

    logger.info(s"${getClass.getSimpleName} startup.")
    logger.info("execution infomations.")
    logger.info("  screen-name = {}", screenName)
    logger.info("  image-type = {}", imageType)
    logger.info("  output-dir = {}", outputBaseDir)
    logger.info("  paging-start = {}", currentPage)
    logger.info("  paging-end = {}", pagingEnd)
    logger.info("  limit = {}", limit)
    logger.info("  output-path-pattern = {}",
      Array(outputBaseDir, "[screen-name]", "[image-type]", "[yyyyMMddHHmmss_image-filename]").mkString("/"))

    val reporter = new ConsoleReporter
    val directoryBuilder = FilePathBuilder(Array(outputBaseDir, screenName, imageType))
    val outputDirectory = directoryBuilder.build

    Files.createDirectories(Paths.get(outputDirectory))

    val httpClient = new HttpClient

    Iterator
      .continually {
        reporter.currentPage(currentPage)
        val paging = new Paging(currentPage, limit)
        currentPage += 1
        twitter.getUserTimeline(screenName, paging)
      }
      .takeWhile { reponseList =>
        if (pagingEnd < 0) !reponseList.isEmpty else (currentPage - 1) <= pagingEnd
      }
      .foreach { responseList =>

        logger.info(s"page[${currentPage - 1}] size = ${responseList.size}")

        responseList.asScala.foreach { status =>
          reporter.incrementTweet()

          val time = formatter.format(status.getCreatedAt)
          val mediaEntries = status.getMediaEntities

          mediaEntries.foreach { mediaEntry =>
            val mediaUrl = s"${mediaEntry.getMediaURL}:${imageType}"

            try {
              httpClient.getInputStream(mediaUrl) { is =>
                val fileName = UrlExtractor.fileNameExcludeType(mediaUrl)
                val filePath = FilePathBuilder(Array(outputDirectory, s"${time}_${fileName}")).build

                val bis = new BufferedInputStream(is)
                val bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(filePath)))

                try {
                  Iterator.continually(bis.read()).takeWhile(_ > -1).foreach(bos.write)
                } finally {
                  bis.close()
                  bos.close()
                }
              }

              reporter.incrementImage()
            } catch {
              case e: IOException => logger.error("download fail, reason = {}", Array(e.getMessage, e))
            }
          }
        }
      }

    reporter.show()
  }
}
