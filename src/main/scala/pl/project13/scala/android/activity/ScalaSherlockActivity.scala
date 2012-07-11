package pl.project13.scala.android.activity

import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{Logging, ViewListenerConversions}
import pl.project13.kanbanery.TypedActivity
import com.actionbarsherlock.app.SherlockActivity
import pl.project13.scala.android.toast.ScalaToasts
import pl.project13.kanbanery.util.Intents

abstract class ScalaSherlockActivity extends SherlockActivity with KanbaneryActivity
  with TypedActivity
  with ImplicitContext with ScalaToasts
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging