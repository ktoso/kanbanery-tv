package pl.project13.android.scala.activity

import android.content.Context

trait ImplicitThisContext { this: Context =>
  implicit val ctx = this
}
