package pl.project13.scala.android.thread

import android.os.Handler
import android.app.Activity
import pl.project13.scala.android.util.{Logging, RunnableConversions}

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
