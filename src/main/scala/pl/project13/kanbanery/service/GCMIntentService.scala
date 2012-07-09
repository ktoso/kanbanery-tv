package pl.project13.kanbanery.service

import android.app.IntentService
import com.google.android.gcm.GCMBaseIntentService
import android.content.{Intent, Context}

class GCMIntentService extends GCMBaseIntentService("pl.project13.kanbanery.permission.C2D_MESSAGE") {

  override def onMessage(p1: Context, p2: Intent) {}

  override def onError(p1: Context, p2: String) {}

  override def onRegistered(p1: Context, p2: String) {}

  override def onUnregistered(p1: Context, p2: String) {}
}
