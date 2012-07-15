package pl.project13.kanbanery.common

import pl.project13.scala.android.util.{Logging, ThisDevice}
import android.content.res.Configuration
import android.app.Activity

trait KanbaneryBoardView {
  this: Logging =>

  /** This depends on the device type and orientation, for TVs it'll be bigger etc */
  def widthOfOneColumn(columns: Int)(implicit act: Activity) = {
    val onOnePage = (ThisDevice.isTv, act.getResources.getConfiguration.orientation) match {
      case (true, _) =>
        debug("We're on a TV, let's use 4 columns")
        4

      case (false, Configuration.ORIENTATION_LANDSCAPE) =>
        debug("We're on a landscape device let's use 2 columns")
        2

      // todo add case for tablets, maybe 3 columns

      case (false, Configuration.ORIENTATION_PORTRAIT) =>
        debug("We're on a portrait device let's use 2 columns")
        1
    }

    val display = act.getWindowManager.getDefaultDisplay
    display.getWidth / onOnePage
  }
}
