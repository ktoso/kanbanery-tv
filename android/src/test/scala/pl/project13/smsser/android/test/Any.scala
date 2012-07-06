package pl.project13.smsser.android.test

import pl.project13.smsser.proto.gen.Sms.{SmsMessageDetails, DeviceId}
import java.util.Date


object Any {

  val MyNumber = "55555555"
  val OtherNumber = "22222222"

  val deviceId: DeviceId =
    DeviceId.newBuilder
      .setId(MyNumber)
      .build()

  def smsMessageDetails(body: String = "Hello"): SmsMessageDetails =
    SmsMessageDetails.newBuilder
      .setFromNumber(MyNumber)
      .setToNumber(OtherNumber)
      .setMsg(body)
      .setSentAt((new Date).getTime.toInt)
      .build()


}
