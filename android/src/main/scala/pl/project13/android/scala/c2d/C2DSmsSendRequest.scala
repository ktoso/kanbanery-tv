//package pl.project13.android.scala.c2d
//
//import android.content.Intent
//import android.util.Config
//
//case class C2DSmsSendRequest(to: String, body: String)
//object C2DSmsSendRequest {
//  def fromIntent(intent: Intent) = {
//    val extras = intent.getExtras
//
//    val account = extras.getString(Config.C2DM_ACCOUNT_EXTRA)
//    val messageExtra = extras.getString(Config.C2DM_MESSAGE_EXTRA)
//
//
//    if (Config.C2DM_MESSAGE_SYNC.equals(messageExtra)) {
//
//    }
//
//    C2DSmsSendRequest("", "")
//  }
//}
