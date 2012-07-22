package pl.project13.kanbanery.fragment

import android.os.{Handler, Bundle}
import android.support.v4.app.Fragment
import android.view._
import android.widget._
import android.widget.FrameLayout.LayoutParams
import pl.project13.janbanery.JanbaneryFromSharedProperties
import pl.project13.janbanery.core.Janbanery
import pl.project13.scala.android.util._
import pl.project13.janbanery.resources.{TaskType, Task, User, Column}
import pl.project13.kanbanery.R
import pl.project13.kanbanery.adapter.TaskArrayAdapter
import collection.JavaConversions._
import android.graphics.drawable.Drawable
import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.annotation.{MonsterDueToJavaApiIntegration, AssuresUiThread}
import scala.Some
import javax.annotation.Nullable
import pl.project13.kanbanery.common.KanbaneryBoardView
import pl.project13.janbanery.util.JanbaneryConversions

/** vars are used as they may be reset when the screen orientation changes */
class ColumnFragment(
    @Nullable var janbanery: Janbanery,
    @Nullable var _column: Column,
    var columns: Int
  ) extends Fragment
  with ViewConversions with ThreadingHelpers with InstanceStateHelpers
  with Logging {

  // will be called when screen orientation is switched
  @MonsterDueToJavaApiIntegration
  def this() {
    this(null, null, 0)
  }

  var columnId = 0L
  val KeyColumnId = InstanceStateKey[Long]("column-id")
  val KeyColumns = InstanceStateKey[Int]("columns")

  lazy val column = Option(_column) match {
    case Some(c) => c
    case _ =>
      info("Trying to obtain kanbanery column with id [%s] ", columnId)
      janbanery.columns.byId(columnId)
  }


  implicit val handler = new Handler

  implicit lazy val ctx = getActivity

  lazy val allUsers = janbanery.users().all()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    janbanery = JanbaneryFromSharedProperties.getUsingApiKey()(getActivity)
    KeyColumnId.tryGet(savedInstanceState) map { columnId = _ }
    KeyColumns.tryGet(savedInstanceState) map { columns = _ }
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val columnView = inflater.inflate(R.layout.column, null).asInstanceOf[LinearLayout]

    type Data = (List[(Task, User, Drawable)], List[TaskType])
    inFuture[Data](whenComplete = initUi(columnView, _: Data)) {
      val tasks = janbanery.tasks.allIn(column)
      val full = tasks map { task =>
        val user = allUsers find(_.getId eq task.getOwnerId) getOrElse new User.NoOne
        val userImage = Drawables.cachedFrom(user.getGravatarUrl)

        (task, user, userImage)
      }

      val taskTypes = janbanery.taskTypes.all()

      (full.toList, taskTypes.toList)
    }

    columnView
  }

  @AssuresUiThread
  def initUi(columnView: LinearLayout, data: (List[(Task, User, Drawable)], List[TaskType])) {
    inUiThread {
      val tasksWithOwners = data._1
      val taskTypes = data._2

      val tasksListView = columnView.find[ListView](R.id.tasks)
      tasksListView.setAdapter(new TaskArrayAdapter(getActivity, tasksWithOwners, taskTypes))

      columnView setLayoutParams new LayoutParams(KanbaneryBoardView.widthOfOneColumn(columns), ViewGroup.LayoutParams.FILL_PARENT)

      info("Updated column [%s] with [%s] tasks", column.getName, tasksWithOwners.size)
    }
  }

  override def onSaveInstanceState(out: Bundle) {
    super.onSaveInstanceState(out)

    saveInstanceStateLong(out)(KeyColumnId, column.getId)
    saveInstanceStateInt(out)(KeyColumns, columns)
  }

}

object ColumnFragment extends Logging {

  def newInstance(janbanery: Janbanery, column: Column, columns: Int): ColumnFragment = {
    info("Creating ColumnFragment for column [%s]", column.getName)

    new ColumnFragment(janbanery, column, columns)
  }

}