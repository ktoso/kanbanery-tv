package pl.project13.kanbanery.activity.dialog

import pl.project13.scala.android.activity.{ContentView, ScalaActivity}
import android.os.{Looper, Handler, Bundle}
import pl.project13.kanbanery.TR
import pl.project13.kanbanery.util.Intents
import pl.project13.janbanery.resources.{User, Task}
import pl.project13.janbanery.JanbaneryFromSharedProperties
import java.util.concurrent.Executors
import collection.JavaConversions._
import pl.project13.janbanery.util.{JanbaneryConversions, JanbaneryAndroidUtils}

class TaskDetailsDialogActivity extends ScalaActivity
  with JanbaneryConversions
  with ContentView {

  implicit val handler = new Handler

  val ContentView = TR.layout.task_details_dialog

  var task: Task = _
  var owner: User = _

  lazy val janbanery = JanbaneryFromSharedProperties.getUsingApiKey()

  lazy val OwnerImage = findView(TR.owner_image)
  lazy val OwnerName = findView(TR.owner_name)
  lazy val TaskId = findView(TR.task_id)
  lazy val TaskType = findView(TR.task_type)
  lazy val Estimate = findView(TR.estimate)
  lazy val TaskDescription = findView(TR.task_description)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    loadIntentExtras()

    setTitle("Details: " + task.getTitle)

    val taskTypes = inFuture { janbanery.taskTypes.all() }
    taskTypes.addListener({
      val taskTypeId = task.getTaskTypeId
      taskTypes.get.find(_.getId == taskTypeId) foreach { taskType =>
        inUiThread { taskType.applyTo(TaskType) }
      }
    }, Executors.newSingleThreadExecutor())


    TaskDescription := task.getDescription
    TaskId := task.getId.toString
  }


  def loadIntentExtras() {
    val extras = getIntent.getExtras
    task = extras.getSerializable(Intents.TaskDetailsDialogActivity.ExtraTask).asInstanceOf[Task]
    owner = extras.getSerializable(Intents.TaskDetailsDialogActivity.ExtraUser).asInstanceOf[User]
  }
}
