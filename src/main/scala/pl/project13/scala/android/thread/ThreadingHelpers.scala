package pl.project13.scala.android.thread

import android.os.{Looper, Handler}
import android.app.ProgressDialog
import pl.project13.scala.android.util.{Logging, RunnableConversions}
import android.content.Context

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

  def inFutureWithProgressDialog(block: => Unit)(implicit ctx: Context, handler: Handler) {
    val dialog = new ProgressDialog(ctx)
    dialog.setMessage("Loading...")
    dialog.show()

    inFuture {
      try {
        block
      } finally  {
        inUiThread { dialog.dismiss() }
      }
    }
  }
}
