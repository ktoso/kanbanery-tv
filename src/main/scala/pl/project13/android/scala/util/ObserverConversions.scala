package pl.project13.android.scala.util

import android.database.ContentObserver
import android.os.Handler

trait ObserverConversions {

  implicit def fun0_contentObserver(fun: () => Unit)(implicit handler: Handler) = fun1_contentObserver((b) => ())

  implicit def fun1_contentObserver(fun: (Boolean) => Unit)(implicit handler: Handler) =
    new ContentObserver(handler) {
      override def onChange(selfChange: Boolean) {
        super.onChange(selfChange)

        fun(selfChange)
      }
    }
}
