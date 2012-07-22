package pl.project13.kanbanery.activity.dialog

import pl.project13.scala.android.activity.{ContentView, ScalaActivity}
import android.os.{Handler, Bundle}
import pl.project13.kanbanery.{R, TR}
import pl.project13.kanbanery.util.Intents
import pl.project13.janbanery.resources._
import pl.project13.janbanery.JanbaneryFromSharedProperties
import collection.JavaConversions._
import pl.project13.janbanery.util.JanbaneryConversions
import pl.project13.scala.android.annotation.AssuresUiThread
import android.widget.ArrayAdapter
import pl.project13.scala.android.util.DisplayInformation

class TaskDetailsDialogActivity extends ScalaActivity
  with JanbaneryConversions
  with ContentView
  with DisplayInformation {

  implicit val handler = new Handler

  val ContentView = TR.layout.task_details_dialog

  var task: Task = _
  var owner: User = _

  lazy val janbanery = JanbaneryFromSharedProperties.getUsingApiKey()

  lazy val Comments = findView(TR.comments)
  lazy val CommentsTitleBar = findView(TR.comments_with_count)
  lazy val NewCommentTxt = findView(TR.new_comment_txt)
  lazy val NewCommentBtn = findView(TR.new_comment)

  lazy val SubTasks = findView(TR.subtasks)
  lazy val SubTasksTitleBar = findView(TR.subtasks_with_count)
  lazy val NewSubTaskBtn = findView(TR.new_subtask)
  lazy val NewSubTaskTxt = findView(TR.new_subtask_txt)

  lazy val OwnerImage = findView(TR.owner_image)
  lazy val OwnerName = findView(TR.owner_name)

  lazy val TaskId = findView(TR.task_id)
  lazy val TaskType = findView(TR.task_type)

  lazy val Estimate = findView(TR.estimate)
  lazy val TaskDescription = findView(TR.task_description)

  // view state
  var comments: List[Comment] = Nil
  var subtasks: List[SubTask] = Nil

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    loadIntentExtras()

    getWindow.setLayout(math.round(displayWidth * 0.8).toInt, math.round(displayHeight * 0.8).toInt)

    setTitle("Details: " + task.getTitle)

    TaskDescription := task.getDescription
    TaskId := task.getId.toString

    loadTaskTypes()
    loadComments()
    loadSubtasks()

    // actions
    NewCommentBtn onClick postNewComment
    NewSubTaskBtn onClick postNewSubTask
  }


  def loadSubtasks() {
    inFuture(whenComplete = renderSubTasks _) { janbanery.subTasks.all(task).toList }
  }

  def loadComments() {
    inFuture(whenComplete = renderComments _) { janbanery.comments.of(task).all().toList }
  }

  def loadTaskTypes() {
    inFuture(whenComplete = renderTaskTypes _) { janbanery.taskTypes.all().toList }
  }

  @AssuresUiThread
  def renderTaskTypes(taskTypes: List[TaskType]) {
    val taskTypeId = task.getTaskTypeId
    taskTypes.find(_.getId == taskTypeId) foreach { taskType =>
      inUiThread { taskType applyTo TaskType }
    }
  }

  @AssuresUiThread
  def renderComments(comments: List[Comment]) {
    this.comments = comments

    inUiThread {
      CommentsTitleBar := "Comments (" + comments.size + ")"
      Comments.setAdapter(new ArrayAdapter(this, R.layout.simple_list_item, R.id.simple_list_txt, comments))
    }
  }

  @AssuresUiThread
  def renderSubTasks(subtasks: List[SubTask]) {
    this.subtasks = subtasks

    inUiThread {
      SubTasksTitleBar := "Subtasks (" + subtasks.size + ")"
      SubTasks.setAdapter(new ArrayAdapter(this, R.layout.simple_list_item, R.id.simple_list_txt, subtasks)) // todo make awesome adapter
    }
  }

  @AssuresUiThread
  def postNewComment() {
    NewCommentTxt.getText.toString match {
      case "" =>
        inUiThread { getString(R.string.cannot_post_empty_comment).toastLong }

      case txt =>
        inFutureWithProgressDialog(getString(R.string.posting_comment), {
          janbanery.comments.of(task) create txt
        })

        comments = txt :: comments
        renderComments(comments)
    }
  }

  @AssuresUiThread
  def postNewSubTask() {
    NewSubTaskTxt.getText.toString match {
      case "" =>
        inUiThread { getString(R.string.cannot_post_empty_subtask).toastLong }

      case txt =>
        inFutureWithProgressDialog(getString(R.string.posting_subtask), {
          janbanery.subTasks.of(task) create txt
        })

        subtasks = txt :: subtasks
        renderSubTasks(subtasks)
    }
  }

  def loadIntentExtras() {
    val extras = getIntent.getExtras
    task = extras.getSerializable(Intents.TaskDetailsDialogActivity.ExtraTask).asInstanceOf[Task]
    owner = extras.getSerializable(Intents.TaskDetailsDialogActivity.ExtraUser).asInstanceOf[User]
  }
}
