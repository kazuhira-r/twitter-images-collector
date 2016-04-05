package org.littlewings.twitterimages

import java.io.File
import java.nio.file.{Files, Paths}
import java.text.SimpleDateFormat

import org.slf4j.LoggerFactory

class Bootstrap

object Bootstrap {
  def main(args: Array[String]): Unit = {
    val parser = StartupOption.newParser("twitter-images-collector")
    val option =
      parser
        .parse(args, StartupOption())
        .getOrElse(sys.exit(1))

    val logger = LoggerFactory.getLogger(classOf[Bootstrap])

    val outputBaseDir = option.outputDir
    val screenName = option.screenName
    var currentPage = option.pagingStart
    val pagingEnd = option.pagingEnd
    val limit = option.limit
    val imageType = option.imageType
    val formatter = new SimpleDateFormat("yyyyMMddHHmmss")

    println {
      s"""|twitter-images-collector startup.
          |
          |execution infomations.
          |  screen-name = ${screenName}
          |  image-type = ${imageType}
          |  output-dir = ${outputBaseDir}
          |  paging-start = ${currentPage}
          |  paging-end = ${if (pagingEnd < 1) "unlimitted" else pagingEnd}
          |  limit = ${limit}
          |  output-path-pattern = ${Array(outputBaseDir, "[screen-name]", "[image-type]", "[yyyyMMddHHmmss]_[id]_[image-filename]").mkString(File.separator)}""".stripMargin
    }

    val outputDirectory = Array(outputBaseDir, screenName, imageType).mkString(File.separator)

    val reporter = new ConsoleReporter
    reporter.imagesOutputDirectory(outputDirectory)

    Files.createDirectories(Paths.get(outputDirectory))

    val collector = new TwitterImagesCollector(option)
    collector.collect(outputDirectory, reporter)

    reporter.show()
  }
}
