package pl.project13.scala.android.service

import android.app.Service
import android.content.Intent
import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{ObserverConversions, Logging}

abstract class ScalaService extends Service with Logging
  with ThreadingHelpers with ObserverConversions {

  def onBind(p1: Intent) = null

}
