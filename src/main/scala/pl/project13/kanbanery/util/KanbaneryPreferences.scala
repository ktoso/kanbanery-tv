package pl.project13.kanbanery.util

import android.content.{SharedPreferences, Context}

object KanbaneryPreferences {

  def sharedPreferences(implicit ctx: Context) = ctx.getSharedPreferences("KanbanerySharedPreferences", 0)

  val KeyLogin = "login"
  val KeyApiKey = "api-key"

  val KeyWorkspaceName = "workspace-name"
  val KeyProjectName = "project-id"

  /** Clears all information required to login to Kanbanery */
  def logout()(implicit ctx: Context) {
    withSharedPreferencesEditor { _.clear().commit() }
  }

  def login(implicit ctx: Context) = sharedPreferences.getString(KeyLogin, "")

  def login_=(number: String)(implicit ctx: Context) {
    withSharedPreferencesEditor { _.putString(KeyLogin, number) }
  }


  def apiKey(implicit ctx: Context) = Option(sharedPreferences.getString(KeyApiKey, null))

  def apiKey_=(key: String)(implicit ctx: Context) {
    withSharedPreferencesEditor { _.putString(KeyApiKey, key) }
  }


  def workspaceName(implicit ctx: Context) = Option(sharedPreferences.getString(KeyWorkspaceName, null))

  def workspaceName_=(name: String)(implicit ctx: Context) {
    withSharedPreferencesEditor { _.putString(KeyWorkspaceName, name) }
  }


  def projectName(implicit ctx: Context): Option[String] = Option(sharedPreferences.getString(KeyProjectName, null))

  def projectName_=(name: String)(implicit ctx: Context) {
    withSharedPreferencesEditor { _.putString(KeyProjectName, name) }
  }

  def withSharedPreferencesEditor(block: SharedPreferences.Editor => Unit)(implicit ctx: Context) {
      val editor = sharedPreferences.edit()
      block(editor)
      editor.commit()
    }
}
