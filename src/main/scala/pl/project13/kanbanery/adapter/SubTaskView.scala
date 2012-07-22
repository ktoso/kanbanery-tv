package pl.project13.kanbanery.adapter

import android.widget.{TextView, CheckBox, CheckedTextView}
import android.view.{LayoutInflater, View}
import android.content.Context
import pl.project13.janbanery.resources.SubTask
import pl.project13.kanbanery.R
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.janbanery.core.Janbanery
import android.app.AlertDialog

object SubTaskView extends ViewListenerConversions {

  val Completed = true
  val NotCompleted = false

  def apply(janbanery: Janbanery, subtask: SubTask)(implicit ctx: Context): View ={
    val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    val v = inflater.inflate(R.layout.subtask_list_item, null)

    // get views
    val SubtaskTxt = v.find[TextView](R.id.subtask_txt)
    val SubtaskCheck = v.find[CheckBox](R.id.subtask_checkbox)

    // set stuff
    SubtaskTxt := subtask.getBody
    SubtaskCheck.setChecked(scala.Boolean.unbox(subtask.getCompleted))

    // listeners
    val Both = SubtaskCheck :: SubtaskTxt :: Nil

    Both foreach { _ onClick {
        scala.Boolean.unbox(subtask.getCompleted) match {
          case Completed => janbanery.subTasks.mark(subtask).asNotCompleted()
          case NotCompleted => janbanery.subTasks.mark(subtask).asCompleted()
        }
      }
    }

    Both foreach { _  onLongClick {
        new AlertDialog.Builder(ctx)
          .setTitle(R.string.should_we_delete_subtask)
          .setPositiveButton(R.string.yes, { janbanery.subTasks.delete(subtask) })
          .setNegativeButton(R.string.no, { })
          .create()

        true
      }
    }

    v
  }

}
