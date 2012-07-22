package pl.project13.scala.android.thread

import android.os.{Looper, Handler}
import android.app.ProgressDialog
import pl.project13.scala.android.util.{Logging, RunnableConversions}
import android.content.Context
import com.google.common.util.concurrent.SettableFuture
import java.util.concurrent.Executors

trait ThreadingHelpers extends RunnableConversions with Logging {
  this: Logging =>

  lazy val singleThreadExecutor = Executors.newSingleThreadExecutor()

  def inUiThread(block: => Unit)(implicit handler: Handler) {
    handler.post({
      debug("Back in UI Thread...")
      block
    })
  }

  def inFuture[T](block: => T)(implicit ctx: Context) : SettableFuture[T] = {
    inFuture[T]()(block)(ctx)
  }

  def inFuture(whenComplete: => Unit)(block: => Unit)(implicit ctx: Context) : SettableFuture[Unit] = {
    inFuture[Unit]((p: Unit) => whenComplete)(block)(ctx)
  }

  def inFuture[T](whenComplete: T => Unit = (t: T) => ())(block: => T)(implicit ctx: Context) : SettableFuture[T] = {
    val future = SettableFuture.create[T]

    future.addListener({ whenComplete(future.get) }, singleThreadExecutor)

    new Thread({
      Looper.prepare()

      try{
        val value = block
        future.set(value)
        debug("Completed inFuture successfuly")
      }catch {
        case e: Exception =>
          warn("Unable to complete inFuture", e)
          future.setException(e)
      }
    }).start()

    future
  }

  def inFutureWithProgressDialog(block: => Unit)(implicit ctx: Context, handler: Handler) {
    inFutureWithProgressDialog("Loading...", block)(ctx, handler)
  }

  def inFutureWithProgressDialog(msg: String, block: => Unit)(implicit ctx: Context, handler: Handler) {
    val dialog = new ProgressDialog(ctx)
    dialog.setMessage(msg)
    dialog.show()

    new Thread({
      Looper.prepare()

      try {
        block
      } finally  {
        inUiThread { dialog.dismiss() }
      }
    }).start()
  }
}
