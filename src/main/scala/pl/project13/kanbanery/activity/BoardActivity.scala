package pl.project13.kanbanery.activity

import collection.JavaConversions._
import pl.project13.scala.android.activity.{ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Looper, Bundle, Handler}
import pl.project13.kanbanery.{R, TR}
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.util.{Intents, KanbaneryPreferences}
import pl.project13.janbanery.core.JanbaneryFactory
import android.content.Context
import android.view.LayoutInflater
import android.widget.{ImageView, ListView, TextView}
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
import android.graphics.drawable.ShapeDrawable
import pl.project13.janbanery.config.{DefaultConfiguration, Configuration}
import pl.project13.scala.android.gcm.GoogleCloudMessaging

class BoardActivity extends ScalaActivity
with ImplicitContext with ScalaToasts
with ViewListenerConversions
with DisplayInformation
with ContentView
with GoogleCloudMessaging {

  implicit val handler = new Handler
  lazy val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

  lazy val BoardView = findView(TR.columns_wrapper)
  lazy val BoardName = findView(TR.board_name)
  lazy val ColumnPager = findView(TR.column_pager)
  lazy val TitleIndicator = findView(TR.titles)

  val ContentView = TR.layout.board


  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    //Set the pager with an adapter


    //Bind the title indicator to the adapter

    TitleIndicator.setViewPager(ColumnPager)
  }

  override def onResume() {
    super.onResume()

    val apiKey = getIntent.getExtras.getString(Intents.BoardActivity.ExtraApiKey)
    val workspaceName = getIntent.getExtras.getString(Intents.BoardActivity.ExtraWorkspaceName)
    val projectName = getIntent.getExtras.getString(Intents.BoardActivity.ExtraProjectName)

    inFutureWithProgressDialog {

      // todo there's a factory for that

      val restClient = new AndroidCompatibleRestClient
      val janbanery = new JanbaneryFactory(restClient).connectUsing(new DefaultConfiguration(apiKey))
        .toWorkspace(workspaceName)
        .usingProject(projectName)

      val allCollumns = janbanery.columns.all()
      allCollumns foreach {
        column =>

          val allUsers: util.List[User] = janbanery.users().all()

          val tasksWithOwners = janbanery.tasks.allIn(column).map {
            task =>
              val user = allUsers.find(_.getId eq task.getOwnerId).getOrElse(new User.NoOne)
              val userImage = loadImageFromWebOperations(user.getGravatarUrl)

              (task, user, userImage)
          }

          inUiThread {
            BoardName := janbanery.projects.current.getName
            val columnView = inflater.inflate(R.layout.column, null)
            //          columnView.findViewById(R.id.column_icon).asInstanceOf[ImageView].

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

  def loadImageFromWebOperations(url: String): Drawable = {
    try {
      val is = new URL(url).getContent.asInstanceOf[InputStream]
      Drawable.createFromStream(is, "src name")
    } catch {
      case e: Exception => null
    }
  }
}