package org.littlewings.twitterimages

import java.io.{BufferedInputStream, BufferedOutputStream, File, IOException}
import java.nio.file.{Files, Paths}
import java.text.{DateFormat, SimpleDateFormat}

import org.slf4j.LoggerFactory
import twitter4j.{Paging, Status, TwitterFactory}

import scala.collection.JavaConverters._

class TwitterImagesCollector(option: StartupOption, outputDirectory: String, reporter: Reporter) {
  val logger = LoggerFactory.getLogger(getClass)

  def collect(): Unit = {
    val twitter = TwitterFactory.getSingleton

    val outputBaseDir = option.outputDir
    val screenName = option.screenName
    var currentPage = option.pagingStart
    val pagingEnd = option.pagingEnd
    val limit = option.limit
    val formatter = new SimpleDateFormat("yyyyMMddHHmmss")

    val httpClient = new HttpClient

    val handler =
      handleStatus(httpClient, formatter) _

    Iterator
      .continually {
        val paging = new Paging(currentPage, limit)
        twitter.getUserTimeline(screenName, paging)
      }
      .takeWhile { reponseList =>
        if (pagingEnd < 0) !reponseList.isEmpty else (currentPage) <= pagingEnd
      }
      .foreach { responseList =>
        reporter.currentPage(currentPage)
        reporter.incrementTweet()

        logger.info(s"page[${currentPage}] size = ${responseList.size}")

        responseList.asScala.foreach(handler)

        currentPage += 1
      }
  }

  protected def handleStatus(httpClient: HttpClient, formatter: DateFormat)(status: Status): Unit = {
    val imageType = option.imageType
    val id = status.getId
    val time = formatter.format(status.getCreatedAt)
    val mediaEntries = status.getMediaEntities

    mediaEntries.foreach { mediaEntry =>
      val mediaUrl = s"${mediaEntry.getMediaURL}:${imageType}"

      try {
        httpClient.getInputStream(mediaUrl) { is =>
          val fileName = UrlExtractor.fileNameExcludeType(mediaUrl)
          val filePath = Array(outputDirectory, s"${time}_${id}_${fileName}").mkString(File.separator)

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
