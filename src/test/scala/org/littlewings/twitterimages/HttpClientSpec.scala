package org.littlewings.twitterimages

import java.io.{ByteArrayInputStream, InputStream}
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets

import com.sun.net.httpserver.HttpServer
import org.scalatest.{FunSpec, Matchers}

import scala.io.Source

class HttpClientSpec extends FunSpec with Matchers {
  describe(classOf[HttpClientSpec].getSimpleName) {
    it("simple case") {
      val httpServer = new FixedHttpServer
      httpServer.start(new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8)))

      try {
        val client = new HttpClient
        client.getInputStream("http://localhost:8080/test") { is =>
          val source = Source.fromInputStream(is, "UTF-8")
          source.mkString should be("Hello World")
          source.close()
        }
      } finally {
        httpServer.stop()
      }
    }
  }
}

class FixedHttpServer(val port: Int = 8080) {
  val httpServer: HttpServer = HttpServer.create(new InetSocketAddress(port), 0)

  def start(handler: => InputStream): Unit = {
    httpServer.createContext("/", exchange => {
      val os = exchange.getResponseBody
      val is = handler
      val bytes = Iterator.continually(is.read()).takeWhile(_ > -1).toArray

      exchange.sendResponseHeaders(200, bytes.size)
      bytes.foreach(os.write)
    })
    httpServer.start()
  }

  def stop(): Unit = httpServer.stop(0)
}