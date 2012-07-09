package pl.project13.scala.android.activity

import android.app.Activity
import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{Logging, ViewListenerConversions}
import pl.project13.kanbanery.TypedActivity
import com.actionbarsherlock.app.SherlockActivity

trait ScalaActivity extends Activity with TypedActivity
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging
