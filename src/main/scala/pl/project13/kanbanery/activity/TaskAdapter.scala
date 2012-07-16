package pl.project13.kanbanery.activity

import android.widget.{EditText, ImageView, TextView, ArrayAdapter}
import android.view.{ViewGroup, View}
import android.content.Context
import pl.project13.janbanery.resources.{TaskType, User, Task}
import collection.JavaConversions._
import pl.project13.kanbanery.R
import pl.project13.janbanery.core.Janbanery
import android.graphics.drawable.Drawable
import pl.project13.scala.android.util.{ViewListenerConversions, ViewConversions}
import pl.project13.janbanery.util.JanbaneryAndroidUtils

class TaskAdapter(ctx: Context, tasks: Seq[(Task, User, Drawable)], taskTypes: List[TaskType])
  extends ArrayAdapter(ctx, R.layout.task, R.id.task_name, tasks)
  with ViewListenerConversions {

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    val v = super.getView(position, convertView, parent)

    val task = tasks.get(position)._1
    val user = tasks.get(position)._2
    val image = tasks.get(position)._3

    val taskNameView = v.find[TextView](R.id.task_name)
    val taskTypeTextView = v.find[TextView](R.id.task_type)
    val ownerImageView = v.find[ImageView](R.id.owner_image)

    taskNameView := task.getTitle
    ownerImageView := image
    initTaskTypeView(taskTypeTextView, task)
    initTaskOnClickListener(v)

    v
  }

  def initTaskTypeView(taskTypeTextView: TextView, task: Task) {
    val taskTypeId = task.getTaskTypeId
    val taskType = taskTypes.find(_.getId == taskTypeId)

    taskType foreach { tt =>
      taskTypeTextView := tt.getName
      taskTypeTextView.setBackgroundColor(JanbaneryAndroidUtils.toAndroidColor(tt.getBackgroundColor))
      taskTypeTextView.setTextColor(JanbaneryAndroidUtils.toAndroidColor(tt.getTextColor))
    }
  }

  def initTaskOnClickListener(taskView: View) {
    taskView onClick {

    }
  }
}
