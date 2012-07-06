package pl.project13.scala.android.activity

import android.content.Context

trait ImplicitThisContext { this: Context =>
  implicit val ctx = this
}
