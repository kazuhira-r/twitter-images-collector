package org.littlewings.twitterimages

import org.scalatest.{FunSpec, Matchers}

class UrlExtractorSpec extends FunSpec with Matchers {
  describe(classOf[UrlExtractorSpec].getSimpleName) {
    it("simple case") {
      UrlExtractor.fileName("http://localhost/foo/bar/hoge.txt") should be("hoge.txt")
    }

    it("with type") {
      UrlExtractor.fileNameExcludeType("http://localhost/foo/bar/hoge.txt:large") should be("hoge.txt")
    }
  }
}
