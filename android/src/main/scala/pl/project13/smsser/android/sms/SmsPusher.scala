package pl.project13.smsser.android.sms

import pl.project13.smsser.android.backend.SmsserBackendClient
import android.content.Context
import pl.project13.smsser.proto.gen.Sms.{DeviceIs, SmsMessageDetails, SmsStoreResponse, SmsStoreRequest}
import java.io.{File, FileOutputStream}
import sun.rmi.runtime.Log
import java.util.Date
import pl.project13.android.scala.util.{Logging, SmsserSharedPreferences, ThisPhone}
import java.text.SimpleDateFormat

class SmsPusher(backendClient: SmsserBackendClient)(implicit val ctx: Context) extends Logging
  with SmsserSharedPreferences {

  val phone = new ThisPhone

  def pushSentSms(sms: SimpleAndroidSms) = {
    pushSms(sms, DeviceIs.SENDER)
  }

  def pushRecievedSms(sms: SimpleAndroidSms) = {
    pushSms(sms, DeviceIs.RECIEVER)
  }


  def pushSms(sms: SimpleAndroidSms, deviceIs: DeviceIs) = {
    val rq = SmsStoreRequest.newBuilder
    .setDeviceIs(deviceIs)
    .setDeviceIdentity(phone.device)
    .addMessages(asSmsMessageDetails(sms, deviceIs))
    .build()

    val eitherExceptionOrResponse = backendClient.postProto[SmsStoreResponse]("/api/sms", rq)

    bumpLastSyncedSmsDate(sms.date)

    eitherExceptionOrResponse
  }

  private def asSmsMessageDetails(sms: SimpleAndroidSms, deviceIs: DeviceIs): SmsMessageDetails = {
    val msg = SmsMessageDetails.newBuilder()
      .setId(sms.id)
      .setMsg(sms.body)
      .setSentAt(sms.date.getTime.toInt)

    deviceIs match {
      case DeviceIs.SENDER =>
        msg
        .setFromNumber(phone.number.get())
        .setToNumber(sms.address)
      case DeviceIs.RECIEVER =>
        msg
        .setFromNumber(sms.address)
        .setToNumber(phone.number.get())
    }

    try {
      msg.build()
    } catch {
      case ex: Exception => throw new RuntimeException("Unable to build SmsMessageDetails ["+deviceIs+"] from: " + sms, ex)
    }
  }

  def bumpLastSyncedSmsDate(date: Date) {
    info("Bumped 'LastSyncedSmsDate' to: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(date))
    sharedPreferencesLastSyncedSmsDate_<<(date)
  }
}
