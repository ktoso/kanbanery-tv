package pl.project13.android.scala.activity

import android.app.Activity
import pl.project13.android.scala.thread.ThreadingHelpers
import pl.project13.android.scala.util.{Logging, ViewListenerConversions}
import pl.project13.kanbanery.tv.TypedActivity

abstract class ScalaActivity extends Activity with TypedActivity
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging
