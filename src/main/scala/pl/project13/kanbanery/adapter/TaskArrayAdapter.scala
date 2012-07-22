package pl.project13.kanbanery.adapter

import android.widget.{EditText, ImageView, TextView, ArrayAdapter}
import android.view.{ViewGroup, View}
import android.content.Context
import pl.project13.janbanery.resources.{TaskType, User, Task}
import collection.JavaConversions._
import pl.project13.kanbanery.R
import pl.project13.janbanery.core.Janbanery
import android.graphics.drawable.Drawable
import pl.project13.scala.android.util.{Logging, ViewListenerConversions, ViewConversions}
import pl.project13.janbanery.util.{JanbaneryConversions, JanbaneryAndroidUtils}
import pl.project13.kanbanery.util.Intents
import pl.project13.scala.android.activity.ScalaSherlockActivity
import pl.project13.scala.android.ui.actionbar.TaskEditActionMode

class TaskArrayAdapter(ctx: Context, tasks: Seq[(Task, User, Drawable)], taskTypes: List[TaskType])
  extends ArrayAdapter(ctx, R.layout.task, R.id.task_name, tasks)
  with ViewListenerConversions with JanbaneryConversions with Logging {

  type TaskData = (Task, User, Drawable)

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    val v = super.getView(position, convertView, parent)

    val taskData = tasks.get(position)
    val task = taskData._1
    val user = taskData._2
    val image = taskData._3

    // get views
    val taskNameView = v.find[TextView](R.id.task_name)
    val taskTypeTextView = v.find[TextView](R.id.task_type)
    val ownerImageView = v.find[ImageView](R.id.owner_image)

    // set stuff
    taskNameView := task.getTitle
    ownerImageView := image
    initTaskTypeView(taskTypeTextView, task)

    // listeners
    initTaskOnClickListener(v, taskData)
    initTaskOnLongClickListener(v, taskData)

    v
  }

  def initTaskTypeView(taskTypeTextView: TextView, task: Task) {
    val taskTypeId = task.getTaskTypeId
    val taskType = taskTypes.find(_.getId == taskTypeId)

    taskType foreach {
      _.applyTo(taskTypeTextView)
    }
  }

  def initTaskOnClickListener(taskView: View, taskData: TaskData) {
    taskView onClick {
      showTaskDetailsView(taskData)
    }
  }

  def initTaskOnLongClickListener(taskView: View, taskData: TaskData) {
    taskView onLongClick {
      ctx.asInstanceOf[ScalaSherlockActivity].startActionMode(new TaskEditActionMode(ctx))
      true
    }
  }

  def showTaskDetailsView(data: TaskData) {
    Intents.TaskDetailsDialogActivity.start(data)(ctx)
  }
}
