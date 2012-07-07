package pl.project13.scala.android.thread

import android.os.{Looper, Handler}
import android.app.Activity
import pl.project13.scala.android.util.{Logging, RunnableConversions}
import com.google.common.util.concurrent.{Futures, ListenableFuture}

trait ThreadingHelpers extends RunnableConversions {
  this: Logging =>

  def inUiThread(block: => Unit)(implicit handler: Handler) {
    handler.post({
      debug("Back in UI Thread...")
      block
    })
  }

  def inFuture(block: => Unit) {
    new Thread({
      Looper.prepare()

      block
    }).start()
  }
}
