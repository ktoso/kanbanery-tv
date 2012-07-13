package pl.project13.kanbanery.activity

import collection.JavaConversions._
import pl.project13.scala.android.activity.{ScalaSherlockActivity, ContentView, ImplicitContext, ScalaActivity}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Looper, Bundle, Handler}
import pl.project13.kanbanery.{R, TR}
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.kanbanery.util.{Intents, KanbaneryPreferences}
import pl.project13.janbanery.core.JanbaneryFactory
import android.content.Context
import android.view.{ViewGroup, View, LayoutInflater}
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
import android.graphics.drawable.{ColorDrawable, Drawable, ShapeDrawable}
import pl.project13.janbanery.config.{DefaultConfiguration, Configuration}
import pl.project13.scala.android.gcm.GoogleCloudMessaging
import android.support.v4.view.PagerAdapter
import pl.project13.kanbanery.adapter.DemoPagerAdapter
import com.actionbarsherlock.view.Window
import pl.project13.janbanery.JanbaneryFromSharedProperties
import pl.project13.janbanery.util.JanbaneryAndroidUtils

class BoardActivity extends ScalaSherlockActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with DisplayInformation {

  implicit val handler = new Handler
  lazy val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

  lazy val BoardView = findView(TR.columns_wrapper)
  lazy val BoardName = findView(TR.board_name)
  lazy val ColumnPager = findView(TR.column_pager)

  val ContentView = TR.layout.board

  lazy val janbanery = JanbaneryFromSharedProperties.getUsingApiKey()

  override def onCreate(bundle: Bundle) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
    super.onCreate(bundle)
    setContentView(ContentView.id)
    val color = new ColorDrawable(JanbaneryAndroidUtils.toAndroidColor("#cc000000"))
    getSupportActionBar.setBackgroundDrawable(color)
  }

  override def onResume() {
    super.onResume()

    val apiKey = getIntent.getExtras.getString(Intents.BoardActivity.ExtraApiKey)
    val workspaceName = getIntent.getExtras.getString(Intents.BoardActivity.ExtraWorkspaceName)
    val projectName = getIntent.getExtras.getString(Intents.BoardActivity.ExtraProjectName)

    KanbaneryPreferences.apiKey = apiKey
    KanbaneryPreferences.workspaceName = workspaceName
    KanbaneryPreferences.projectName = projectName

    setTitle("Kanbanery Tv - " + workspaceName + " / " + projectName)

    inFutureWithProgressDialog {
      janbanery
        .usingWorkspace(workspaceName)
        .usingProject(projectName)

      val allUsers = janbanery.users().all()

      val allColumns = janbanery.columns.all()
      allColumns foreach {
        column =>

          info("Rendering column [%s]", column.getName)

          val tasksWithOwners = janbanery.tasks.allIn(column).map { task =>
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

            BoardView.addView(columnView, widthOfOneColumn(allColumns.size), displayHeight)
          }
      }
    }
  }


  protected override def onDestroy() {
    super.onDestroy()
    janbanery.close()
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

  override def onTrimMemory(p1: Int) {}
}