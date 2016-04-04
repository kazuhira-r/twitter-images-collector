package org.littlewings.twitterimages

import scopt.OptionParser

case class StartupOption(outputDir: String = ".",
                         screenName: String = "",
                         imageType: String = "large",
                         pagingStart: Int = 1,
                         pagingEnd: Int = -1,
                         limit: Int = 200)

object StartupOption {
  def newParser[T](fromClass: Class[T]): OptionParser[StartupOption] = {
    new OptionParser[StartupOption](fromClass.getSimpleName) {
      opt[String]('o', "output-dir") valueName ("<output-dir>") action { (x, o) =>
        o.copy(outputDir = x)
      } text ("images ouptput directory [default .(current directory)]")

      opt[String]('s', "screen-name") required() valueName ("<screen-name>") action { (x, o) =>
        o.copy(screenName = x)
      } text ("Twitter screen name [required]")

      opt[String]('t', "image-type") valueName ("<image-type>") action { (x, o) =>
        o.copy(imageType = x)
      } text ("download image type [default large, choise [small, thumb, medium, large]]")

      opt[Int]('s', "paging-start") valueName ("<paging-start>") action { (x, o) =>
        o.copy(pagingStart = x)
      } text ("paging start [default 1]")

      opt[Int]('e', "paging-end") valueName ("<paging-end>") action { (x, o) =>
        o.copy(pagingEnd = x)
      } text ("paging end [default -1(unlimitted)")

      opt[Int]('l', "limit") valueName ("<limit>") action { (x, o) =>
        o.copy(limit = x)
      } text ("per page get count [default and max 200]")

      checkConfig { o =>
        if (o.pagingEnd > 0 && o.pagingStart > o.pagingEnd) failure("invalid spec paging, start > end")
        else success
      }
    }
  }
}