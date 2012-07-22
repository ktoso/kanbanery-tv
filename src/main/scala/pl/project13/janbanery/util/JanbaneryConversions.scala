package pl.project13.janbanery.util

import pl.project13.janbanery.resources.{SubTask, Comment, Column, TaskType}
import android.widget.TextView

trait JanbaneryConversions {

  implicit def str2comment(s: String) = new Comment(s)
  implicit def str2commentList(s: String) = List(new Comment(s))

  implicit def str2subtask(s: String) = new SubTask(s)
  implicit def str2subtaskList(s: String) = List(new SubTask(s))

  implicit def columnHasFancyName(column: Column) = new AndroidFriendlyColumn(column)

  implicit def taskTypeToAndroidFriendly(taskType: TaskType) = new AndroidFriendlyTaskType(taskType)

  class AndroidFriendlyTaskType(taskType: TaskType) {
    def applyTo(view: TextView) {
      view.setText(taskType.getName)
      view.setBackgroundColor(JanbaneryAndroidUtils.toAndroidColor(taskType.getBackgroundColor))
      view.setTextColor(JanbaneryAndroidUtils.toAndroidColor(taskType.getTextColor))
    }
  }

  class AndroidFriendlyColumn(column: Column) {

    def getName(tasksInColumn: Int): String = {
      val limit = Option(column.getCapacity) match {
        case Some(cap) => " (" + tasksInColumn + " / " + cap + ")"
        case _ => " " + tasksInColumn
      }

      column.getName + limit
    }
  }

}
