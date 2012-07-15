package pl.project13.scala.android.util

import android.graphics.drawable.Drawable
import java.net.URL
import java.io.InputStream
import com.google.common.cache._
import java.util.concurrent.TimeUnit
import android.content.res.Resources
import pl.project13.kanbanery.R

object Drawables extends Logging {

  private val cache: LoadingCache[String, Drawable] = CacheBuilder.newBuilder
    .weakValues
    .expireAfterAccess(10, TimeUnit.MINUTES)
    .build(new CacheLoader[String, Drawable]() {
      def load(url: String) = loadFrom(url)
    })

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run() {
      cache.cleanUp()
    }
  })

  private def loadFrom(url: String): Drawable = {
    try {
      val is = new URL(url).getContent.asInstanceOf[InputStream]
      Drawable.createFromStream(is, "src name")
    } catch {
      case e: Exception =>
        error("Unable to download image", e)
        null
    }
  }

  def cachedFrom(url: String): Drawable = {
    cache.get(url)
  }
}
