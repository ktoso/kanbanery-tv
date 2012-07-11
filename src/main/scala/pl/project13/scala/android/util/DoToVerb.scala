package pl.project13.scala.android.util

import pl.project13.janbanery.core.Janbanery

trait DoToVerb {

  def doTo[T](it: T)(block: T => Unit): T = {
    block(it)
    it
  }

}

object DoToVerb extends DoToVerb