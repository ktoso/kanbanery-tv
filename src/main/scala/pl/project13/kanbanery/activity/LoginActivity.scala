package pl.project13.kanbanery.activity

import pl.project13.scala.android.activity.{ScalaSherlockActivity, ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.{R, TR}
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.util.{Intents, KanbaneryPreferences}
import pl.project13.janbanery.core.JanbaneryFactory
import android.content.Intent

class LoginActivity extends ScalaSherlockActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with ContentView {

  implicit val handler = new Handler

  val ContentView = TR.layout.main

  lazy val SignInButton = findView(TR.sign_in_btn)
  lazy val EmailEditText = findView(TR.email_edit_text)
  lazy val PassEditText = findView(TR.pass_edit_text)

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    // todo remove me
    EmailEditText := "ghack@project13.pl"
    PassEditText := "123456"

    SignInButton.requestFocus()

    SignInButton onClick {
      val login = EmailEditText.getText.toString
      val pass = PassEditText.getText.toString

      Intents.ProjectSelectionActivity.start(login, pass)
      finish()
    }
  }
}