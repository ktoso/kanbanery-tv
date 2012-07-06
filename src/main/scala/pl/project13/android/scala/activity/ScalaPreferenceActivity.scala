package pl.project13.android.scala.activity

import pl.project13.android.scala.thread.ThreadingHelpers
import pl.project13.android.scala.util.{Logging, ViewListenerConversions}
import android.preference.PreferenceActivity
import pl.project13.kanbanery.tv.TypedActivity

abstract class ScalaPreferenceActivity extends PreferenceActivity with TypedActivity
  with ViewListenerConversions
  with ThreadingHelpers
  with Logging
