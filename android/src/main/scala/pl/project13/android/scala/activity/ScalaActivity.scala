package pl.project13.android.scala.activity

import android.app.Activity
import pl.project13.smsser.android.TypedActivity
import pl.project13.android.scala.thread.ThreadingHelpers
import pl.project13.android.scala.util.{RunnableConversions, Logging, ViewListenerConversions}

abstract class ScalaActivity extends Activity with TypedActivity
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging
