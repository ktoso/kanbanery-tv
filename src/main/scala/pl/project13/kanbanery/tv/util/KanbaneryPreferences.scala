package pl.project13.kanbanery.tv.util

import android.content.{SharedPreferences, Context}
import java.util

object KanbaneryPreferences {

  def sharedPreferences(implicit ctx: Context) = ctx.getSharedPreferences("KanbanerySharedPreferences", 0)

  val KeyLogin = "login"
  val KeyPass = "pass"

  def login(implicit ctx: Context) = sharedPreferences.getString(KeyLogin, "")

  def login_=(number: String)(implicit ctx: Context) {
    withSharedPreferencesEditor { _.putString(KeyLogin, number) }
  }

  def pass(implicit ctx: Context) = sharedPreferences.getString(KeyPass, "")

  def pass_=(pass: String)(implicit ctx: Context) {
    withSharedPreferencesEditor { _.putString(KeyPass, pass) }
  }

  def withSharedPreferencesEditor(block: SharedPreferences.Editor => Unit)(implicit ctx: Context) {
      val editor = sharedPreferences.edit()
      block(editor)
      editor.commit()
    }
}
