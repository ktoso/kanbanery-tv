package pl.project13.smsser.android.reciever

import android.content.{Intent, Context, BroadcastReceiver}
import pl.project13.smsser.android.backend.{BackendConfig, SmsserBackendClient}
import pl.project13.android.scala.toast.ScalaToasts
import pl.project13.android.scala.thread.ThreadingHelpers
import pl.project13.android.scala.util.{SmsserSharedPreferences, Logging}
import pl.project13.smsser.android.sms.SmsSender
import android.util.Config

class Cloud2DeviceReciever extends BroadcastReceiver with Logging
  with ScalaToasts with ThreadingHelpers {

  def backendClient(ctx: Context)= new SmsserBackendClient(BackendConfig.loadFromResources(ctx))

  val Registration = "com.google.android.c2dm.intent.REGISTRATION"
  val Recieve = "com.google.android.c2dm.intent.RECEIVE"

  def onReceive(ctx: Context, intent: Intent) {
    intent.getAction match {
      case Registration =>
//        val request = C2DRegisterRequest.fromIntent(intent)
//        handleRegistration(ctx, request)
        handleRegistration(ctx, intent)

      case Recieve =>
//        val request = C2DSmsSendRequest.fromIntent(intent)
//        handleMessage(ctx, request)
    }
  }

//  def handleMessage(context: Context, sendRequest: C2DSmsSendRequest) {
//     SmsSender.send(sendRequest.to, sendRequest.body)
//   }

  def handleRegistration(ctx: Context, intent: Intent) {
    implicit val x = ctx

    val registration = intent.getStringExtra("registration_id")
    if (intent.getStringExtra("error") != null) {

      "Unable to register in clould2device service...".toastLong

    } else if (intent.getStringExtra("unregistered") != null) {

      // unregistration done, new messages from the authorized sender will be rejected

    } else if (registration != null) {

//        inFuture {
//          backendClient(ctx).postProto()
//        }

      // Send the registration ID to the 3rd party site that is sending the messages.
      // This should be done in a separate thread.
      // When done, remember that all registration is done.

    }
  }

}

case class C2DRegisterRequest()

