package pl.project13.kanbanery.tv.activity

import pl.project13.android.scala.activity.{ContentView, ImplicitContext, ScalaActivity}
import pl.project13.android.scala.toast.ScalaToasts
import android.os.Handler
import pl.project13.kanbanery.tv.TR

class LoginActivity extends ScalaActivity with ImplicitContext
  with ScalaToasts with ContentView {

  implicit val handler = new Handler

  val ContentView = TR.layout.main


}