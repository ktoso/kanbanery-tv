package pl.project13.kanbanery.util

import android.content.{Context, Intent}
import pl.project13.kanbanery.activity.{BoardPhoneActivity, LoginActivity, ProjectSelectionActivity, BoardActivity}
import pl.project13.scala.android.util.ThisDevice
import pl.project13.janbanery.resources.{User, Task}
import android.graphics.drawable.Drawable
import pl.project13.kanbanery.activity.dialog.TaskDetailsDialogActivity

object Intents {

  object LoginActivity {

    def logout()(implicit ctx: Context) {
      KanbaneryPreferences.logout()
      start()
    }

    def start()(implicit ctx: Context) {
      val intent = new Intent(ctx, classOf[LoginActivity])
      ctx.startActivity(intent)
    }
  }

  object BoardActivity {

    val ExtraApiKey = "apiKey"
    val ExtraWorkspaceName = "workspaceName"
    val ExtraProjectName = "projectName"

    def start(apiKey: String, workspaceName: String, projectName: String)(implicit ctx: Context) {
      val target = if(ThisDevice.isTv) {
        classOf[BoardActivity]
      } else {
        classOf[BoardPhoneActivity]
      }

      val intent = new Intent(ctx, target)
      intent.putExtra(ExtraApiKey, apiKey)
      intent.putExtra(ExtraWorkspaceName, workspaceName)
      intent.putExtra(ExtraProjectName, projectName)
      ctx.startActivity(intent)
    }
  }

  object ProjectSelectionActivity {
    val ExtraLogin = "login"
    val ExtraPass = "pass"
    val ExtraApiKey = "apikey"

    def start(apiKey: String)(implicit ctx: Context) {
      val intent = new Intent(ctx, classOf[ProjectSelectionActivity])
      intent.putExtra(ExtraApiKey, apiKey)
      ctx.startActivity(intent)
    }

    def start(login: String, pass: String)(implicit ctx: Context) {
      val intent = new Intent(ctx, classOf[ProjectSelectionActivity])
      intent.putExtra(ExtraLogin, login)
      intent.putExtra(ExtraPass, pass)
      ctx.startActivity(intent)
    }
  }

  object TaskDetailsDialogActivity {
    val ExtraTask = "task"
    val ExtraUser = "user"

    def start(data: (Task, User, Drawable))(implicit ctx: Context) {
      val intent = new Intent(ctx, classOf[TaskDetailsDialogActivity])
      intent.putExtra(ExtraTask, data._1)
      intent.putExtra(ExtraUser, data._2)
      ctx.startActivity(intent)
    }
  }

}
