package pl.project13.kanbanery.tv.activity

import android.widget.{ImageView, TextView, ArrayAdapter}
import android.view.{ViewGroup, View}
import android.content.Context
import pl.project13.janbanery.resources.{User, Task}
import collection.JavaConversions._
import pl.project13.kanbanery.tv.R
import pl.project13.janbanery.core.Janbanery
import android.graphics.drawable.Drawable

class TaskAdapter(ctx: Context, tasks: Seq[(Task, User, Drawable)])
  extends ArrayAdapter(ctx, R.layout.task, R.id.task_name, tasks) {

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    val v = super.getView(position, convertView, parent)

    val task = tasks.get(position)._1
    val user = tasks.get(position)._2
    val image = tasks.get(position)._3

    v.findViewById(R.id.task_name).asInstanceOf[TextView].setText(task.getTitle)
    v.findViewById(R.id.owner_name).asInstanceOf[TextView].setText(user.getFirstName)
    v.findViewById(R.id.task_type).asInstanceOf[TextView].setText(task.getTaskTypeName)
    v.findViewById(R.id.owner_image).asInstanceOf[ImageView].setImageDrawable(image)

    v
  }
}
