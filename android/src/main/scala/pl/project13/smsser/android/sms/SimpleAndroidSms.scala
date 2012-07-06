package pl.project13.smsser.android.sms

import java.util.Date
import android.telephony.SmsMessage
import java.awt.TrayIcon.MessageType

/**
 * '''
 * D/SmsDb$  (  714): Found 1 sms messages.
 * D/SmsserActivity(  714):  _id:3
 * D/SmsserActivity(  714):  thread_id:2
 * D/SmsserActivity(  714):  address:12345
 * D/SmsserActivity(  714):  person:null
 * D/SmsserActivity(  714):  date:1335826016261
 * D/SmsserActivity(  714):  protocol:0
 * D/SmsserActivity(  714):  read:0
 * D/SmsserActivity(  714):  status:-1
 * D/SmsserActivity(  714):  type:1
 * D/SmsserActivity(  714):  reply_path_present:0
 * D/SmsserActivity(  714):  subject:null
 * D/SmsserActivity(  714):  body:'helo'
 * D/SmsserActivity(  714):  service_center:null
 * D/SmsserActivity(  714):  locked:0
 * D/SmsserActivity(  714):  error_code:0
 * D/SmsserActivity(  714):  seen:0
 * '''
 */
case class SimpleAndroidSms(
  id: Long,
  threadId: Long,
  address: String,
  date: Date,
  read: Int,
  body: String,
  messageType: SimpleAndroidSmsType.Value
)

object SimpleAndroidSms {

  def fromMap(map: Map[String, String]) =
    SimpleAndroidSms(
      id = map("_id").toLong,
      threadId = map("thread_id").toLong,
      address = map("address"),
      date = new Date(map("date").toLong),
      read = map("read").toInt,
      body = map("body"),
      messageType = SimpleAndroidSmsType.fromAndroidId(map("type").toInt)
    )
}
