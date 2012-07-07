package pl.project13.kanbanery.tv.activity

import android.widget.{TextView, ArrayAdapter}
import android.view.{ViewGroup, View}
import android.content.Context
import pl.project13.janbanery.resources.Task
import collection.JavaConversions._
import pl.project13.kanbanery.tv.R

class TaskAdapter(ctx: Context, tasks: Seq[Task])
  extends ArrayAdapter(ctx, R.layout.task, R.id.task_name, tasks) {

  override def getView(position: Int, convertView: View, parent: ViewGroup) = {
    val v = super.getView(position, convertView, parent)

    val task = tasks.get(position)

    v.findViewById(R.id.task_name).asInstanceOf[TextView].setText(task.getTitle)

    v
  }
}
