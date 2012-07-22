package pl.project13.janbanery.util

import pl.project13.janbanery.resources._
import android.widget.TextView
import scala.Some

trait JanbaneryConversions {

  implicit def user2androidFriendlyUser(u: User) = new AndroidFriendlyUser(u)

  implicit def str2comment(s: String) = new Comment(s)
  implicit def str2commentList(s: String) = List(new Comment(s))

  implicit def str2subtask(s: String) = new SubTask(s)
  implicit def str2subtaskList(s: String) = {
    val t = new SubTask(s)
    t setCompleted false
    List(t)
  }

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

  class AndroidFriendlyUser(user: User) {
    def getDisplayName = user.getFirstName + " " + user.getLastName
  }

}
