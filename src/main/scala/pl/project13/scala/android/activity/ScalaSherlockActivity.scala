package pl.project13.scala.android.activity

import pl.project13.scala.android.thread.ThreadingHelpers
import pl.project13.scala.android.util.{Logging, ViewListenerConversions}
import pl.project13.kanbanery.TypedActivity
import com.actionbarsherlock.app.SherlockActivity

abstract class ScalaSherlockActivity extends SherlockActivity
  with ScalaActivity
