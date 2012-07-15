package pl.project13.scala.android.util

import android.app.Activity
import android.graphics.Point

trait DisplayInformation {
  this: Activity =>

  private lazy val display = getWindowManager.getDefaultDisplay

  lazy val displayWidth = display.getWidth
  lazy val displayHeight = display.getHeight
}
