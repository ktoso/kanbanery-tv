package pl.project13.scala.android.util

import android.content.Context

object ThisDevice {

  def isTv()(implicit ctx: Context): Boolean = {
    ctx.getPackageManager.hasSystemFeature("com.google.android.tv")
  }

  def isNotTv()(implicit ctx: Context): Boolean =
  !isTv()
}
