package pl.project13.scala.android.util

import java.io.Closeable
import pl.project13.janbanery.core.Janbanery


trait DoWithVerb {

  def doWith[T <: Closeable](it: T)(block: T => Unit) {
    block(it)
    it.close()
  }

  def doWith(it: Janbanery)(block: Janbanery => Unit) {
    block(it)
    it.close()
  }

}

object DoWithVerb extends DoWithVerb