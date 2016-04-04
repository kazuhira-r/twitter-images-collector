package org.littlewings.twitterimages

import org.scalatest.{FunSpec, Matchers}

class FilePathBuilderSpec extends FunSpec with Matchers {
  describe(classOf[FilePathBuilder].getSimpleName) {
    it("normary") {
      val builder = FilePathBuilder("base")
      builder += ("foo")
      builder += ("bar")
      builder += ("hoge.txt")

      builder.build should be ("base/foo/bar/hoge.txt")
    }

    it("spec separator") {
      val builder = FilePathBuilder("base")
      builder += ("foo")
      builder += ("bar")
      builder += ("hoge.txt")

      builder.build("|") should be ("base|foo|bar|hoge.txt")
    }

    it("multiple path") {
      val builder = FilePathBuilder(Array("foo", "bar", "hoge"))
      builder.build should be ("foo/bar/hoge")
    }

    it("copy") {
      val builder1 = FilePathBuilder("foo")
      builder1 += "hoge"

      val builder2 = builder1.copy
      builder2 += "fuga"

      builder1.build should be ("foo/hoge")
      builder2.build should be ("foo/hoge/fuga")
    }
  }
}
