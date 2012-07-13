package pl.project13.kanbanery.activity

import pl.project13.scala.android.activity.{ScalaSherlockActivity, ImplicitContext}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.{R, TR}
import pl.project13.scala.android.util.{ThisDevice, ViewListenerConversions}
import pl.project13.kanbanery.util.{KanbaneryTitle, Intents, KanbaneryPreferences}
import android.content.Context
import android.view.{ViewGroup, LayoutInflater}
import collection.JavaConversions._
import pl.project13.scala.android.tv.DisplayInformation
import pl.project13.janbanery.resources.User
import java.net.URL
import java.io.InputStream
import android.graphics.drawable.{ColorDrawable, Drawable}
import com.actionbarsherlock.view.Window
import pl.project13.janbanery.JanbaneryFromSharedProperties
import pl.project13.janbanery.util.JanbaneryAndroidUtils
import android.support.v4.app.FragmentManager
import android.widget.LinearLayout.LayoutParams
import android.widget.{ImageView, ListView}
import pl.project13.kanbanery.fragment.ColumnFragment

class BoardActivity extends ScalaSherlockActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with DisplayInformation {

  implicit val handler = new Handler
  lazy val Inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

//  lazy val PageIndicator = findView(TR.page_indicator)
  lazy val ColumnsContainer = findView(TR.column_pager)
  lazy val VisibleColumnsIndicator = findView(TR.page_indicator)

  val ContentView = TR.layout.board

  lazy val janbanery = JanbaneryFromSharedProperties.getUsingApiKey()

  override def onCreate(bundle: Bundle) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
    requestWindowFeature(Window.FEATURE_PROGRESS)

    super.onCreate(bundle)
    setContentView(ContentView.id)
    val color = new ColorDrawable(JanbaneryAndroidUtils.toAndroidColor("#aa000000"))
    getSupportActionBar.setBackgroundDrawable(color)

    findView(TR.main).setPadding(0, getSupportActionBar.getHeight, 0, 0)
  }

  override def onResume() {
    super.onResume()

    val apiKey = getIntent.getExtras.getString(Intents.BoardActivity.ExtraApiKey)
    val workspaceName = getIntent.getExtras.getString(Intents.BoardActivity.ExtraWorkspaceName)
    val projectName = getIntent.getExtras.getString(Intents.BoardActivity.ExtraProjectName)

    KanbaneryPreferences.apiKey = apiKey
    KanbaneryPreferences.workspaceName = workspaceName
    KanbaneryPreferences.projectName = projectName

    KanbaneryTitle.set(workspace = workspaceName, project = projectName)

    inFutureWithProgressDialog {
      janbanery
        .usingWorkspace(workspaceName)
        .usingProject(projectName)

      val allUsers = janbanery.users().all()
      val allColumns = janbanery.columns.all().toList

      inUiThread { VisibleColumnsIndicator.setColumns(allColumns) }

//      val supportFragmentManager = getSupportFragmentManager
//      val columnAdapter = new ColumnAdapter(janbanery, supportFragmentManager, allColumns.toList)

      var columnsFetchProgress = 1
      allColumns foreach { column =>
        handler.post {
          setSupportProgress(columnsFetchProgress / allColumns.size)
        }

        info("Rendering column [%s]", column.getName)

        val tasksWithOwners = janbanery.tasks.allIn(column).map { task =>
          val user = allUsers.find(_.getId eq task.getOwnerId).getOrElse(new User.NoOne)
          val userImage = loadImageFromWebOperations(user.getGravatarUrl)

          (task, user, userImage)
        }

//        inUiThread {
//          val columnView = inflater.inflate(R.layout.column, null)
          //          columnView.findViewById(R.id.column_icon).asInstanceOf[ImageView].
//
//          val tasksListView = columnView.findViewById(R.id.tasks).asInstanceOf[ListView]
//          tasksListView.setAdapter(new TaskAdapter(this, tasksWithOwners))
//
//          ColumnPager.addView(columnView, widthOfOneColumn(allColumns.size), displayHeight)
//          columns += columnView
//        }
//      }
        columnsFetchProgress += 1
      }

      inUiThread {
        if(ThisDevice.isTv){

          allColumns foreach { column =>
            val tx = getSupportFragmentManager.beginTransaction()
            val columnFragment = ColumnFragment.newInstance(janbanery, column, allColumns.size)

  //          val columnView = Inflater.inflate(R.layout.column, null)
  //
  //          val tasksListView = columnView.findViewById(R.id.tasks).asInstanceOf[ListView]
  //          tasksListView.setAdapter(new TaskAdapter(this, tasksWithOwners))
  //
  //          ColumnsContainer.addView(columnView, widthOfOneColumn(allColumns.size), displayHeight)

            tx.add(ColumnsContainer.getId, columnFragment, "column-" + column.getId)
            tx.commit()
          }

        } else {
        // for phones
//          ColumnPager.setAdapter(columnAdapter)
          ColumnsContainer.setLayoutParams(new LayoutParams(widthOfOneColumn(allColumns.size), ViewGroup.LayoutParams.FILL_PARENT))
//          PageIndicator.setViewPager(ColumnPager)
        }


//      }
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