package pl.project13.android.scala.thread

import android.os.Handler
import android.app.Activity
import pl.project13.android.scala.util.{Logging, RunnableConversions}

trait ThreadingHelpers extends RunnableConversions {
  this: Logging =>

  def inUiThread(block: => Unit)(implicit handler: Handler) {
    handler.post({
      debug("Back in UI Thread...")
      block
    })
  }

  def inFuture(block: => Unit) {
    new Thread(block).start()
  }
}
