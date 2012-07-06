package pl.project13.smsser.android.backend

import android.content.Context
import pl.project13.smsser.android.R

case class BackendConfig(host: String, port: Int) {
  val url = "http://" + host + ":" + port
}

object BackendConfig {
  def loadFromResources(ctx: Context) = {
    val host = ctx.getString(R.string.server_host)
    val port = ctx.getString(R.string.server_port).toInt

    BackendConfig(host, port)
  }
}
