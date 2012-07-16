package pl.project13.scala.android.activity

import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{ThisDevice, Logging, ViewListenerConversions}
import pl.project13.kanbanery.TypedActivity
import com.actionbarsherlock.app.{SherlockFragmentActivity, SherlockActivity}
import pl.project13.scala.android.toast.ScalaToasts
import com.actionbarsherlock.internal
import android.os.Bundle
import com.actionbarsherlock.view.Window
import pl.project13.kanbanery.activity.{BoardPhoneActivity, BoardActivity}
import pl.project13.kanbanery.util.Intents.BoardActivity

abstract class ScalaSherlockActivity extends SherlockFragmentActivity with KanbaneryActivity
  with TypedActivity
  with ImplicitContext with ScalaToasts
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