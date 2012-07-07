package pl.project13.kanbanery.tv.activity

import pl.project13.scala.android.activity.{ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.tv.TR
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.tv.util.KanbaneryPreferences
import pl.project13.janbanery.core.JanbaneryFactory

class BoardActivity extends ScalaActivity
with ImplicitContext with ScalaToasts
with ViewListenerConversions
with ContentView {

  implicit val handler = new Handler

  val ContentView = TR.layout.main

  lazy val SignInButton = findView(TR.sign_in_btn)
  lazy val EmailEditText = findView(TR.email_edit_text)

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    SignInButton onClick {
      KanbaneryPreferences.login = EmailEditText.getText.toString
    }
  }
}