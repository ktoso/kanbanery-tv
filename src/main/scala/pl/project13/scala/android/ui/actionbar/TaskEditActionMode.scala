package pl.project13.scala.android.ui.actionbar

import android.content.Context
import com.actionbarsherlock.view.{MenuItem, Menu, ActionMode}
import android.widget.Toast

class TaskEditActionMode(ctx: Context) extends ActionMode.Callback {

  val GroupIdModify = 1
  val GroupIdMove = 2

  override def onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = {
    menu.add("Edit")
      //                .setIcon(R.drawable.ic_compose)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    menu.add("Search")
      //        .setIcon(R.drawable.ic_search)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    // move actions
    menu.add(GroupIdMove, ActionItemIds.MoveLeft, 1, pl.project13.kanbanery.R.string.move_left)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(GroupIdMove, ActionItemIds.MoveSomewhere, 2, pl.project13.kanbanery.R.string.move_to_selected)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    menu.add(GroupIdMove, ActionItemIds.MoveRight, 3, pl.project13.kanbanery.R.string.move_right)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)


    menu.add("Save")
      //                .setIcon(R.drawable.ic_compose)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    true
  }

  override def onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = {
    false
  }

  override def onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean = {
    item.getItemId match {
      case ActionItemIds.MoveLeft =>
        toast("Move left")

      case ActionItemIds.MoveSomewhere =>
        toast("Move somewhere")

      case ActionItemIds.MoveRight =>
        toast("Move right")

      case _ =>
        toast("Got click: " + item)
        mode.finish()
    }
    true
  }


  def toast(msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
  }

  override def onDestroyActionMode(mode: ActionMode) {
  }
}