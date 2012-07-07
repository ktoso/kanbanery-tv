package pl.project13.kanbanery.tv.util

import android.content.{Context, Intent}
import pl.project13.kanbanery.tv.activity.BoardActivity

object Intents {

  def startBoard(login: String, pass: String)(implicit ctx: Context) {
    val intent = new Intent(ctx, classOf[BoardActivity])
    intent.putExtra("login", login)
    intent.putExtra("pass", pass)
    ctx.startActivity(intent)
  }
}
