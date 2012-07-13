package pl.project13.kanbanery.util

import android.content.{Context, Intent}
import pl.project13.kanbanery.activity.{LoginActivity, ProjectSelectionActivity, BoardActivity}

object Intents {

  object LoginActivity {
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
      val intent = new Intent(ctx, classOf[BoardActivity])
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

}
