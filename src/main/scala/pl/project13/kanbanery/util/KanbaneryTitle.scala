package pl.project13.kanbanery.util

import android.content.{SharedPreferences, Context}
import android.app.Activity
import pl.project13.scala.android.util.ThisDevice

object KanbaneryTitle {

  def set()(implicit ctx: Activity)  {
    ctx.setTitle(kanbaneryName)
  }
  def set(workspace: String, project: String)(implicit ctx: Activity)  {
    ctx.setTitle(workspace + " / " + project)
  }

  def kanbaneryName(implicit ctx: Context): String = {
    "Kanbanery" + (if (ThisDevice.isTv) " Tv" else "") + ": "
  }
}

