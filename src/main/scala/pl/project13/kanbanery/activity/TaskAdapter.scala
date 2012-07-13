package pl.project13.kanbanery.activity

import android.widget.{EditText, ImageView, TextView, ArrayAdapter}
import android.view.{ViewGroup, View}
import android.content.Context
import pl.project13.janbanery.resources.{User, Task}
import collection.JavaConversions._
import pl.project13.kanbanery.R
import pl.project13.janbanery.core.Janbanery
import android.graphics.drawable.Drawable
import pl.project13.scala.android.util.ViewConversions

class TaskAdapter(ctx: Context, tasks: Seq[(Task, User, Drawable)])
  extends ArrayAdapter(ctx, R.layout.task, R.id.task_name, tasks)
  with ViewConversions {

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    val v = super.getView(position, convertView, parent)

    val task = tasks.get(position)._1
    val user = tasks.get(position)._2
    val image = tasks.get(position)._3

    v.find[TextView](R.id.task_name) := task.getTitle
//    v.find[TextView](R.id.task_type) := task.getTaskTypeName
//    v.find[TextView](R.id.owner_name) := List(user.getFirstName, user.getLastName).mkString(" ")
    v.find[ImageView](R.id.owner_image) := image

    v
  }
}
