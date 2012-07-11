package pl.project13.kanbanery.activity

import pl.project13.scala.android.activity._
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.{R, TR}
import pl.project13.scala.android.util.{Logging, ViewListenerConversions}
import pl.project13.kanbanery.util.{Intents, KanbaneryPreferences}
import pl.project13.janbanery.core.JanbaneryFactory
import android.content.Intent
import com.actionbarsherlock.app.SherlockActivity
import com.actionbarsherlock.{internal, ActionBarSherlock}
import com.actionbarsherlock.internal.{ActionBarSherlockNative, ActionBarSherlockCompat}

class LoginActivity extends ScalaActivity
  with ViewListenerConversions
  with ContentView {

  implicit val handler = new Handler

  val ContentView = TR.layout.login

  lazy val SignInButton = findView(TR.sign_in_btn)
  lazy val EmailEditText = findView(TR.email_edit_text)
  lazy val PassEditText = findView(TR.pass_edit_text)

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    (KanbaneryPreferences.apiKey, KanbaneryPreferences.workspaceName, KanbaneryPreferences.projectName) match {
      case (Some(apiKey), Some(workspaceName), Some(projectName)) => Intents.BoardActivity.start(apiKey, workspaceName, projectName); finish()
      case (Some(apiKey), _, _) => Intents.ProjectSelectionActivity.start(apiKey); finish()
      case _ => // continue the login process...
    }

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