package pl.project13.scala.android.util

import android.widget._
import android.view.View
import android.graphics.drawable.Drawable
import android.net.Uri

trait ViewConversions {

  implicit def pimpTextView(btn: TextView) = new PimpedTextView(btn)
  implicit def pimpImageView(btn: ImageView) = new PimpedImageView(btn)
  implicit def viewHasTypesFinds(v: View) = new HasTypedFinds(v)

  class HasTypedFinds(v: View) {
    def find[Type <: View](r: Int) = v.findViewById(r).asInstanceOf[Type]
  }

  class PimpedTextView(v: TextView) {
    def :=(s: String) {
      v.setText(s)
    }
  }

  class PimpedImageView(v: ImageView) {
    def :=(d: Drawable) {
      v.setImageDrawable(d)
    }

    def :=(u: Uri) {
      v.setImageURI(u)
    }
  }
}
