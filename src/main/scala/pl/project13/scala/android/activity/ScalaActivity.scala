package pl.project13.scala.android.activity

import android.app.Activity
import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{Logging, ViewListenerConversions}
import pl.project13.kanbanery.TypedActivity
import com.actionbarsherlock.app.SherlockActivity
import pl.project13.kanbanery.util.Intents
import pl.project13.scala.android.toast.ScalaToasts

trait ScalaActivity extends Activity with TypedActivity with KanbaneryActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging