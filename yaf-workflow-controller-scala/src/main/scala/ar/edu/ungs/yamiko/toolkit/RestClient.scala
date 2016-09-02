package ar.edu.ungs.yamiko.toolkit

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import scala.collection.mutable.StringBuilder
import org.apache.http.impl.client.HttpClientBuilder
import java.net.URLEncoder

object RestClient {

    val httpClient = HttpClientBuilder.create().build();

    /**
   * Returns the text content from a REST URL. Returns a blank String if there
   * is a problem.
   */
  def getRestContent(url:String): String = {
    val httpResponse = httpClient.execute(new HttpGet(url))
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      val inputStream = entity.getContent()
      content = scala.io.Source.fromInputStream(inputStream).getLines.mkString
      inputStream.close
    }
    //httpClient.close()
    return content
    
  }
}