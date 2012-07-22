package pl.project13.janbanery

import android.rest.AndroidCompatibleRestClient
import com.google.common.cache.{Cache, CacheBuilder}
import java.util.concurrent.TimeUnit
import pl.project13.scala.android.util.Logging
import java.lang.reflect.Type

trait Caching extends AndroidCompatibleRestClient with Logging {

  override def doGet[T](url: String, `type`: Type): T = synchronized {
    debug("Trying cached GET for " + url)

    Caching.cache.getIfPresent(url) match {
      case null =>
        val response = super.doGet[T](url, `type`)
        Caching.cache.put(url, response.asInstanceOf[Object])
        response

      case it =>
        it.asInstanceOf[T]
    }
  }
}

object Caching {
  val cache: Cache[String, Object] = CacheBuilder.newBuilder
    .concurrencyLevel(1)
    .expireAfterWrite(10, TimeUnit.SECONDS)
    .build()
}