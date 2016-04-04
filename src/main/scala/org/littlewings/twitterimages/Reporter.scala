package org.littlewings.twitterimages

object Reporter {
  def defaultFormat(reporter: Reporter): String =
    s"""|executed infomations.
        |  collected total page = ${reporter.page}
        |  collected total image count = ${reporter.imageCount}""".stripMargin
}

trait Reporter {
  protected var page: Int = _
  protected var imageCount: Int = _

  def currentPage(page: Int): Unit = this.page = page

  def addImageCount(imageCount: Int): Unit = this.imageCount += imageCount

  def show(): Unit
}

class ConsoleReporter extends Reporter {
  def show(): Unit = System.out.println(Reporter.defaultFormat(this))
}
