package pl.project13.scala.android.activity

import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{ThisDevice, Logging, ViewListenerConversions}
import pl.project13.kanbanery.TypedActivity
import com.actionbarsherlock.app.SherlockActivity
import pl.project13.scala.android.toast.ScalaToasts
import com.actionbarsherlock.internal
import android.os.Bundle
import com.actionbarsherlock.view.Window

abstract class ScalaSherlockActivity extends SherlockActivity with KanbaneryActivity
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
  }

}