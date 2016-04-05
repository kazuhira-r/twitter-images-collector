package org.littlewings.twitterimages

object Reporter {
  def defaultFormat(reporter: Reporter): String =
    s"""|executed infomations.
        |  scanned total page = ${reporter.page}
        |  scanned total tweet count = ${reporter.tweetCount}
        |  collected total image count = ${reporter.imageCount}
        |  images output directory = ${reporter.outputDirectory}""".stripMargin
}

trait Reporter {
  protected var outputDirectory: String = _
  protected var page: Int = _
  protected var tweetCount: Int = _
  protected var imageCount: Int = _

  def imagesOutputDirectory(dir: String): Unit = this.outputDirectory = dir

  def currentPage(page: Int): Unit = this.page = page

  def incrementTweet(): Unit = tweetCount += 1

  def incrementImage(): Unit = imageCount += 1

  def show(): Unit
}

class ConsoleReporter extends Reporter {
  def show(): Unit = println(Reporter.defaultFormat(this))
}
