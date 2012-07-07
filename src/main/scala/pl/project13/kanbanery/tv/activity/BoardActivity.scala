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
import android.widget.{ListView, TextView}
import pl.project13.janbanery.android.rest.AndroidCompatibleRestClient
import android.util.Log
import collection.JavaConversions._
import collection.JavaConverters._
import android.view.ViewGroup.LayoutParams
import pl.project13.scala.android.tv.DisplayInformation
import java.util
import pl.project13.janbanery.resources.User
import java.net.URL
import java.io.InputStream
import android.graphics.drawable.Drawable

class BoardActivity extends ScalaActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with DisplayInformation
  with ContentView {

  implicit val handler = new Handler
  lazy val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

  lazy val BoardView = findView(TR.columns_wrapper)
  lazy val BoardName = findView(TR.board_name)

  val ContentView = TR.layout.board

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
  }

  override def onResume() {
    super.onResume()

    val login = getIntent.getExtras.getString("login")
    val pass = getIntent.getExtras.getString("pass")

    inFuture {
      val restClient = new AndroidCompatibleRestClient
      val janbanery = new JanbaneryFactory(restClient).connectUsing(login, pass).toWorkspace("ghack")

      janbanery.usingProject("ghack")

      val allCollumns = janbanery.columns.all()
      allCollumns foreach { column =>

        val allUsers: util.List[User] = janbanery.users().all()

        val tasksWithOwners = janbanery.tasks.allIn(column).map { task =>
          val user = allUsers.find( _.getId eq task.getOwnerId).getOrElse(new User.NoOne)
          val userImage = LoadImageFromWebOperations(user.getGravatarUrl)

          (task, user, userImage)
        }

        inUiThread {
          BoardName := janbanery.projects.current.getName

          val columnView = inflater.inflate(R.layout.column, null)
          columnView.findViewById(R.id.column_icon).asInstanceOf[TextView].setText(column.getName)

          val tasksListView = columnView.findViewById(R.id.tasks).asInstanceOf[ListView]
          tasksListView.setAdapter(new TaskAdapter(this, tasksWithOwners))

          BoardView.addView(columnView, widthOfOneColumn(allCollumns.size), displayHeight)
        }
      }
    }
  }

  def widthOfOneColumn(columns: Int) =
    if (columns > 4) displayWidth / 4
    else displayWidth / columns

  def LoadImageFromWebOperations(url: String) {
    try {
    val is = (InputStream) new URL(url).getContent();
    val d = Drawable.createFromStream(is, "src name");
    return d;
    } catch { case e: Exception => }
  }
}