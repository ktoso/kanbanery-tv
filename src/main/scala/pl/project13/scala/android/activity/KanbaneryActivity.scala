package pl.project13.scala.android.activity

import android.app.Activity
import pl.project13.kanbanery.util.Intents
import pl.project13.scala.android.util.Logging
import android.content.Context

trait KanbaneryActivity {
  this: Activity with Logging =>

  implicit def ctx: Context

  def startLoginActivityOnException[T](block: => T) = {
    try {
      block
    } catch {
      case ex: Exception =>
        error(ex, "Got exception and redirecting to LoginActivity...")
        Intents.LoginActivity.start()
        finish()
    }
  }

}
