package pl.project13.android.scala.activity

import android.app.Activity
import pl.project13.smsser.android.TypedActivity
import pl.project13.android.scala.thread.ThreadingHelpers
import pl.project13.android.scala.util.{Logging, ViewListenerConversions}
import android.preference.PreferenceActivity

abstract class ScalaPreferenceActivity extends PreferenceActivity with TypedActivity
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging
