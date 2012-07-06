package pl.project13.scala.android.activity

import android.app.Activity

trait ImplicitContext { this: Activity =>
  implicit val ctx = this
}
