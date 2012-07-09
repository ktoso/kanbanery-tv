package pl.project13.scala.android.activity

import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{Logging, ViewListenerConversions}
import android.preference.PreferenceActivity
import pl.project13.kanbanery.TypedActivity

abstract class ScalaPreferenceActivity extends PreferenceActivity with TypedActivity
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging
