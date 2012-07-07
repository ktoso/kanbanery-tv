package pl.project13.kanbanery.tv.activity

import pl.project13.scala.android.activity.{ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.tv.{R, TR}
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.tv.util.{Intents, KanbaneryPreferences}
import pl.project13.janbanery.core.JanbaneryFactory
import android.content.Intent

class LoginActivity extends ScalaActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with ContentView {

  implicit val handler = new Handler

  val ContentView = TR.layout.main

  lazy val SignInButton = findView(TR.sign_in_btn)
  lazy val EmailEditText = findView(TR.email_edit_text)
  lazy val PassEditText = findView(TR.email_edit_text)

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    SignInButton onClick {
      val login = EmailEditText.getText.toString
      val pass = PassEditText.getText.toString

      Intents.startBoard(login, pass)
      finish()
    }
  }
}