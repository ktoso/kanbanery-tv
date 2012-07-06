package pl.project13.android.scala.c2d

import android.content.Intent
import android.app.{Activity, PendingIntent}
import pl.project13.smsser.android.R

class Cloud2DeviceUtil {

  def register(act: Activity) {
    act.getString(R.string.c2d_account)

    val intent = new Intent("com.google.android.c2dm.intent.REGISTER");
    intent.putExtra("app", PendingIntent.getBroadcast(act, 0, new Intent, 0)); // boilerplate
    intent.putExtra("sender", "ktosopl@gmail.com")
    act.startService(intent)
  }

  def unregister(act: Activity) {
    val intent = new Intent("com.google.android.c2dm.intent.UNREGISTER")
    intent.putExtra("app", PendingIntent.getBroadcast(act, 0, new Intent, 0)) // boilerplate
    act.startService(intent)
  }

}
