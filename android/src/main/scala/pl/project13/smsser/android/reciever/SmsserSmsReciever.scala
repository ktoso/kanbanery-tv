package pl.project13.smsser.android.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

import pl.project13.android.scala.toast.ScalaToasts._
import pl.project13.android.scala.util.{SmsExtractor, Logging}
import pl.project13.android.scala.activity.ImplicitContext
import pl.project13.android.scala.thread.ThreadingHelpers
import pl.project13.smsser.android.backend.{BackendConfig, SmsserBackendClient}
import pl.project13.smsser.android.sms.{SimpleAndroidSms, SmsPusher}
import pl.project13.smsser.android.service.SyncSmsService

class SmsserSmsReciever extends BroadcastReceiver with Logging with ThreadingHelpers {

  val smsExtractor = new SmsExtractor

  def onReceive(context: Context, intent: Intent) {
    implicit val ctx = context

    val messages = smsExtractor.extractMessages(intent)

    messages foreach { sms =>
      val str = "SMS from [%s], body: %s".format(sms.getOriginatingAddress, sms.getMessageBody.toString)

      info("Smsser: " + str)

      str.toastLong
    }

    triggerSync()
  }

  def triggerSync()(implicit ctx: Context) {
    ctx.startService(new Intent(ctx, classOf[SyncSmsService]))
  }

}
