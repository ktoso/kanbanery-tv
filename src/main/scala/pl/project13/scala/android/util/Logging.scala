package pl.project13.scala.android.util

import android.util.Log

trait Logging {

  private lazy val Tag = getClass.getSimpleName

  def safe(items: Seq[Any]): Seq[Any] = {
    items map { _ match {
        case s: String => s.toString.replaceAll("%", "%%")
        case any => any
      }
    }
  }

  def debug(msg: String, interpolations: Any*) {
    Log.d(Tag, msg.format(safe(interpolations): _*))
  }

  def debug(th: Throwable, msg: String, interpolations: Any*) {
    Log.d(Tag, msg.format(safe(interpolations): _*), th)
  }

  def info(msg: String, interpolations: Any*) {
    Log.i(Tag, msg.format(safe(interpolations): _*))
  }

  def info(th: Throwable, msg: String, interpolations: Any*) {
    Log.i(Tag, msg.format(safe(interpolations): _*), th)
  }

  def warn(msg: String, interpolations: Any*) {
    Log.w(Tag, msg.format(safe(interpolations): _*))
  }

  def warn(th: Throwable, msg: String, interpolations: Any*) {
    Log.w(Tag, msg.format(safe(interpolations): _*), th)
  }

  def error(msg: String, interpolations: Any*) {
    Log.e(Tag, msg.format(safe(interpolations): _*))
  }

  def error(th: Throwable, msg: String, interpolations: Any*) {
    Log.e(Tag, msg.format(safe(interpolations): _*), th)
  }

  def wtf(msg: String, interpolations: Any*) {
    Log.wtf(Tag, msg.format(safe(interpolations): _*))
  }

  def wtf(th: Throwable, msg: String, interpolations: Any*) {
    Log.wtf(Tag, msg.format(safe(interpolations): _*), th)
  }

}
