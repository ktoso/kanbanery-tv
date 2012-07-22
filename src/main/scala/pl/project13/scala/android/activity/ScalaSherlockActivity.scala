package pl.project13.scala.android.activity

import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{ThisDevice, Logging, ViewListenerConversions}
import pl.project13.kanbanery.{R, TypedActivity}
import com.actionbarsherlock.app.{SherlockFragmentActivity, SherlockActivity}
import pl.project13.scala.android.toast.ScalaToasts
import com.actionbarsherlock.internal
import android.os.Bundle
import com.actionbarsherlock.view.{ActionMode, Menu, MenuItem, Window}
import pl.project13.kanbanery.activity.{BoardPhoneActivity, BoardActivity}
import pl.project13.kanbanery.util.Intents.BoardActivity
import pl.project13.scala.android.ui.actionbar.ActionItemIds
import pl.project13.kanbanery.util.{KanbaneryPreferences, Intents}

abstract class ScalaSherlockActivity extends SherlockFragmentActivity with KanbaneryActivity with TypedActivity
  with ImplicitContext
  with ScalaToasts
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging {

  // proguard workaround ... Details here: https://groups.google.com/forum/?fromgroups#!topic/scala-on-android/V8pgBphHaOg
  lazy val actionBarSherlockNativeProguardHack = new internal.ActionBarSherlockNative(this, 0)
  lazy val actionBarSherlockCompatProguardHack = new internal.ActionBarSherlockCompat(this, 0)

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    initHomeAsUpButton()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    //    menu.add("Save")
    //      .setIcon(R.drawable.ic_compose)
    //      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    //    menu.add("Search")
    //      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT)

    //    menu.add("Refresh")
    //      .setIcon(R.drawable.ic_refresh)
    //      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT)

    val subMenu = menu.addSubMenu("More")

    val logoutMenuItem = subMenu.add(1, ActionItemIds.Logout, 1, R.string.logout)
    logoutMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    logoutMenuItem.getItemId

    val subMenu1Item = subMenu.getItem
    subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark)
    subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT)


    super.onCreateOptionsMenu(menu)
  }

  override def onOptionsItemSelected(item: MenuItem) = {
    item.getItemId match {
      case ActionItemIds.Logout =>
        Intents.LoginActivity.logout()
        finish()
        true

      case _ =>
        super.onOptionsItemSelected(item)
    }
  }

  def initHomeAsUpButton() {
    val makeHomeAsUpEnabled = this.getClass match {
      case e: BoardActivity => true
      case e: BoardPhoneActivity => true
      case _ => false
    }

    getSupportActionBar.setDisplayHomeAsUpEnabled(makeHomeAsUpEnabled)
  }

  def setSupportProgressPercent(progressPercent: Int) {
    val progress = (android.view.Window.PROGRESS_END - android.view.Window.PROGRESS_START) / 100 * progressPercent
    info("Progress @ " + progress + "%%")
    super.setSupportProgress(progress)
  }
}