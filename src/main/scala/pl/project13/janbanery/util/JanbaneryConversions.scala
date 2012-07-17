package pl.project13.janbanery.util

import pl.project13.janbanery.resources.TaskType
import android.widget.TextView

trait JanbaneryConversions {

  implicit def taskTypeToAndroidFriendly(taskType: TaskType) = new AndroidFriendlyTaskType(taskType)

  class AndroidFriendlyTaskType(taskType: TaskType) {
    def applyTo(view: TextView) {
      view.setText(taskType.getName)
      view.setBackgroundColor(JanbaneryAndroidUtils.toAndroidColor(taskType.getBackgroundColor))
      view.setTextColor(JanbaneryAndroidUtils.toAndroidColor(taskType.getTextColor))
    }
  }
}
