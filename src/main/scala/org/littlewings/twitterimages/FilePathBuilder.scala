package org.littlewings.twitterimages

import scala.collection.mutable.ArrayBuffer

object FilePathBuilder {
  def apply(path: String): FilePathBuilder = new FilePathBuilder(path)

  def apply(paths: Seq[String]): FilePathBuilder = new FilePathBuilder(paths)
}

class FilePathBuilder(paths: Seq[String]) {
  val builder: ArrayBuffer[String] = ArrayBuffer.empty ++ paths

  def this(path: String) = this(Array(path))

  def append(path: String): this.type = {
    builder += path
    this
  }

  def += (path: String): this.type = append(path)

  def copy: FilePathBuilder = new FilePathBuilder(builder.clone)

  def build: String = build("/")

  def build(delimiter: String): String =
    builder.mkString(delimiter)
}
