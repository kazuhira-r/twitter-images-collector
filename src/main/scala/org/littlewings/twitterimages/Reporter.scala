package org.littlewings.twitterimages

object Reporter {
  def defaultFormat(reporter: Reporter): String =
    s"""|executed infomations.
        |  scaned total page = ${reporter.page}
        |  scaned total tweet count = ${reporter.tweetCount}
        |  collected total image count = ${reporter.imageCount}""".stripMargin
}

trait Reporter {
  protected var page: Int = _
  protected var tweetCount: Int = _
  protected var imageCount: Int = _

  def currentPage(page: Int): Unit = this.page = page

  def incrementTweet(): Unit = tweetCount += 1

  def incrementImage(): Unit = imageCount += 1

  def show(): Unit
}

class ConsoleReporter extends Reporter {
  def show(): Unit = System.out.println(Reporter.defaultFormat(this))
}
