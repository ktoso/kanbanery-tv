package pl.project13.kanbanery.tv.activity

import collection.JavaConversions._
import pl.project13.scala.android.activity.{ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Looper, Bundle, Handler}
import pl.project13.kanbanery.tv.{R, TR}
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.tv.util.KanbaneryPreferences
import pl.project13.janbanery.core.JanbaneryFactory
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import pl.project13.janbanery.android.rest.AndroidCompatibleRestClient

class BoardActivity extends ScalaActivity
with ImplicitContext with ScalaToasts
with ViewListenerConversions
with ContentView {

  implicit val handler = new Handler
  lazy val boardView = findView(TR.columns_wrapper)
  lazy val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
  val ContentView = TR.layout.board

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

  }

  override def onResume() {
    super.onResume()

    val login = getIntent.getExtras.getString("login")
    val pass = getIntent.getExtras.getString("pass")


    inFuture {
      Looper.prepare()
      val restClient = new AndroidCompatibleRestClient
      val using = new JanbaneryFactory(restClient).connectUsing(login, pass)

      val workspace = using.toWorkspace("ghack")
      workspace.usingProject("ghack")
      workspace.columns().all().foreach { column =>

        inUiThread {
          val columnView = inflater.inflate(R.layout.column, boardView)
          columnView.findViewById(R.id.column).asInstanceOf[TextView].setText(column.getName)
          boardView.addView(columnView)
        }
      }
    }
  }
}