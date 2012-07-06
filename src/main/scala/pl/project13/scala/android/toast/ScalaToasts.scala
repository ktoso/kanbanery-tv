package pl.project13.scala.android.toast

import android.content.Context

trait ScalaToasts {

  implicit def charSeq2toastable(str: java.lang.CharSequence) = new Toastable(str.toString)

  class Toastable(msg: String) {

    import android.widget.Toast._

    def toastLong(implicit ctx: Context) { makeText(ctx, msg, LENGTH_LONG).show() }
    def toastShort(implicit ctx: Context) { makeText(ctx, msg, LENGTH_SHORT).show() }
  }

}

object ScalaToasts extends ScalaToasts
