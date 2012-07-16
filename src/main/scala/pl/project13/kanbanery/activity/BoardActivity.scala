package pl.project13.kanbanery.activity

import pl.project13.scala.android.activity.{ScalaSherlockActivity, ImplicitContext}
import pl.project13.scala.android.toast.ScalaToasts
import android.os.{Bundle, Handler}
import pl.project13.kanbanery.TR
import pl.project13.scala.android.util.{DisplayInformation, ThisDevice, ViewListenerConversions}
import pl.project13.kanbanery.util.{KanbaneryTitle, Intents, KanbaneryPreferences}
import android.content.Context
import android.view.LayoutInflater
import collection.JavaConversions._
import android.graphics.drawable.ColorDrawable
import com.actionbarsherlock.view.Window
import pl.project13.janbanery.JanbaneryFromSharedProperties
import pl.project13.janbanery.util.JanbaneryAndroidUtils
import pl.project13.kanbanery.fragment.ColumnFragment
import pl.project13.kanbanery.common.KanbaneryBoardView

class BoardActivity extends ScalaSherlockActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with KanbaneryBoardView
  with DisplayInformation {

  implicit val handler = new Handler
  lazy val Inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

//  lazy val PageIndicator = findView(TR.page_indicator)
  lazy val ColumnsContainer = findView(TR.columns_container)
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

      val allColumns = janbanery.columns.all().toList

      inUiThread { VisibleColumnsIndicator.setColumns(allColumns) }

      inUiThread {
        setSupportProgress(0)
        val eachColumnGivesXPercent = 100 / allColumns.size

        ColumnsContainer.removeAllViews()

        var loadedColumnCount = 0
        allColumns foreach { column =>
          handler.post {
            val tx = getSupportFragmentManager.beginTransaction()
            val columnFragment = ColumnFragment.newInstance(janbanery, column, allColumns.size)

            tx.add(ColumnsContainer.getId, columnFragment, "column-" + column.getId)
            tx.commit()

            loadedColumnCount += 1
            setSupportProgressPercent(loadedColumnCount * eachColumnGivesXPercent)
          }
        }
      }
    }
  }


  protected override def onDestroy() {
    super.onDestroy()
    janbanery.close()
  }

  override def onTrimMemory(p1: Int) {}
}