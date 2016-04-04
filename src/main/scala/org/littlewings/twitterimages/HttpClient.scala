package org.littlewings.twitterimages

import java.io.InputStream

import okhttp3.{OkHttpClient, Request}

class HttpClient {
  val client: OkHttpClient = new OkHttpClient

  def getInputStream(url: String)(fun: InputStream => Unit): Unit = {
    val request =
      new Request
      .Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()

    try {
      fun(response.body.byteStream())
    } finally {
      response.body.close()
    }
  }
}
