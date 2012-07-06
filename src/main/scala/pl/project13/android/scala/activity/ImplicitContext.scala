package pl.project13.android.scala.activity

import android.app.Activity

trait ImplicitContext { this: Activity =>
  implicit val ctx = this
}
