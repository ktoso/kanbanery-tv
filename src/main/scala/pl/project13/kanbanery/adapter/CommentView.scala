package pl.project13.kanbanery.adapter

import android.widget.TextView
import android.view.{LayoutInflater, View}
import android.content.Context
import pl.project13.janbanery.resources.{User, Comment}
import pl.project13.kanbanery.R
import pl.project13.scala.android.util.ViewListenerConversions
import pl.project13.janbanery.core.Janbanery
import android.app.AlertDialog
import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.janbanery.util.JanbaneryConversions
import pl.project13.scala.android.annotation.AssuresUiThread
import android.os.Handler

object CommentView
  extends ViewListenerConversions
  with ThreadingHelpers
  with JanbaneryConversions {

  implicit val handler = new Handler

  def apply(janbanery: Janbanery, comment: Comment)(implicit ctx: Context): View = {
    val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    val v = inflater.inflate(R.layout.simple_list_item, null)

    // get views
    val Text = v.find[TextView](R.id.simple_list_txt)
    Text := comment.getBody

    // update the comment text with the autor
    inFuture(whenComplete = setCommentText(Text, comment, _: User)) {
      janbanery.users.byId(comment.getAuthorId)
    }

    // listeners
    Text onLongClick {
      new AlertDialog.Builder(ctx)
        .setTitle(R.string.should_we_delete_comment)
        .setPositiveButton(R.string.yes, { janbanery.comments.delete(comment) })
        .setNegativeButton(R.string.no, {})
        .create()

      true
    }

    v
  }

  @AssuresUiThread
  def setCommentText(v: TextView, comment: Comment, u: User) {
    inUiThread { v := comment.getBody + " by " + u.getDisplayName }
  }

}
