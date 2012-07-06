package pl.project13.android.scala.util

import android.view.View
import android.view.View.OnClickListener
import android.widget.{TextView, Button}
import android.content.DialogInterface

trait ViewListenerConversions {

  // views
  implicit def buttonHasOnClick(btn: Button) = new HasOnClick(btn)

  implicit def textViewCanHaveText(btn: TextView) = new TextSettable(btn)

  // listeners

  implicit def fun1_onClickListener(block: => Unit) = new OnClickListener {
    def onClick(p1: View) { block }
  }

  implicit def fun1_Dialog_onClickListener[T](block: () => T) = new DialogInterface.OnClickListener {
    def onClick(p1: DialogInterface, p2: Int) { block() }
  }

  class HasOnClick(btn: Button) {
    def onClick(block: => Unit) { btn setOnClickListener block }
  }

  class TextSettable(v: TextView) {
    def :=(s: String) {
      v.setText(s)
    }
  }
}
