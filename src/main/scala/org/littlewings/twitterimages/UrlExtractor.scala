package org.littlewings.twitterimages

import java.io.File
import java.net.URL

object UrlExtractor {
  def fileName(url: String): String =
    new File(new URL(url).getFile).getName

  def fileNameExcludeType(url: String): String = {
    val name = fileName(url)

    if (name.contains(":")) name.split(':')(0)
    else name
  }
}
