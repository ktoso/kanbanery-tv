package pl.project13.kanbanery.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view._
import android.widget._
import android.widget.LinearLayout.LayoutParams
import pl.project13.janbanery.JanbaneryFromSharedProperties
import pl.project13.janbanery.core.Janbanery
import pl.project13.scala.android.util.{ViewConversions, Logging}
import pl.project13.janbanery.resources.{Task, User, Column}
import android.content.Context
import pl.project13.kanbanery.R
import pl.project13.kanbanery.activity.TaskAdapter
import collection.JavaConversions._
import android.graphics.drawable.Drawable
import java.net.URL
import java.io.InputStream
import com.google.common.collect.MapMaker
import scala.Some

class ColumnFragment(janbanery: Janbanery, column: Column, columns: Int) extends Fragment with ViewConversions with Logging {

  import ColumnFragment._

  implicit lazy val ctx = getActivity

  lazy val allUsers = janbanery.users().all()

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    //    if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
    //      mContent = savedInstanceState.getString(KEY_CONTENT)
    //    }
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val columnView = inflater.inflate(R.layout.column, null)

    val tasks = janbanery.tasks.allIn(column)
    val tasksWithOwners = tasks map { task =>
      info("Preparing hydrated task: %s", task.getTitle)

      val user = allUsers find(_.getId eq task.getOwnerId) getOrElse new User.NoOne
      val userImage = loadImageFromWebOperations(user.getGravatarUrl)

      (task, user, userImage)
    }

    val columnName = columnView.find[TextView](R.id.column_label)
    columnName := mkName(column, tasks.size)

    val tasksListView = columnView.find[ListView](R.id.tasks)
    tasksListView.setAdapter(new TaskAdapter(getActivity, tasksWithOwners))

    columnView setLayoutParams new ViewGroup.LayoutParams(widthOfOneColumn(columns), ViewGroup.LayoutParams.FILL_PARENT)
    columnView
  }

  override def onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
  }


  def mkName(column: Column, taskCount: Int): String = {
    import column._
    val limit = Option(getCapacity) match {
      case Some(cap) => " (" + taskCount + " / " + cap + ")"
      case _ => ""
    }

    getName + limit
  }

  def widthOfOneColumn(columns: Int) = {
      val display = getActivity.getWindowManager.getDefaultDisplay
    if (columns >= 4)  display.getWidth / 4
    else display.getWidth / columns
  }
}

object ColumnFragment extends Logging {

  val imagesCache = (new MapMaker)
    .weakKeys
    .weakValues
    .makeMap[String, Drawable]()

  def newInstance(janbanery: Janbanery, column: Column, columns: Int): ColumnFragment = {
    info("Creating ColumnFragment for column [%s]", column.getName)

    new ColumnFragment(janbanery, column, columns)
  }

  def loadImageFromWebOperations(url: String): Drawable = {
    try {
      imagesCache(url) match {
        case drawable if drawable != null => drawable
        case _ => Drawable.createFromStream(new URL(url).getContent.asInstanceOf[InputStream], "src name")
      }
    } catch {
      case e: Exception => null
    }
  }

}