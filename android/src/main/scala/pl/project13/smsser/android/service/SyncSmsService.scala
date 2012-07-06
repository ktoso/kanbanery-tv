package pl.project13.smsser.android.service

import android.content.Intent
import pl.project13.smsser.android.backend.{SmsserBackendClient, BackendConfig}
import pl.project13.smsser.android.sms.{SmsPusher, SmsDb}
import pl.project13.android.scala.service.ScalaService
import pl.project13.android.scala.activity.{ImplicitThisContext, ImplicitContext}
import pl.project13.android.scala.util.{Logging, ThisPhone}
import android.os.Handler
import android.database.ContentObserver

class SyncSmsService extends ScalaService
  with ImplicitThisContext with Logging {

  val smsDb = SmsDb

  val phone = new ThisPhone

  implicit lazy val handler = new Handler

  lazy val smsObserver: ContentObserver = (selfUpdate: Boolean) => { pushAll() }

  lazy val backendConf = BackendConfig.loadFromResources(this)
  lazy val smsserBackendClient = new SmsserBackendClient(backendConf)
  lazy val smsPusher = new SmsPusher(smsserBackendClient)


  override def onCreate() {
    super.onCreate()

    registerSmsContentListener()
  }

  override def onDestroy() {
    unregisterSmsContentListener()

    super.onDestroy()
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int) = {
    info("SyncSmsService triggered, will sync messages.")

    pushAll()

    super.onStartCommand(intent, flags, startId)
  }

  def registerSmsContentListener() {
    getContentResolver.registerContentObserver(SmsDb.SmsAllUri, true, smsObserver)
  }

  def unregisterSmsContentListener() {
    getContentResolver.unregisterContentObserver(smsObserver)
  }

  def pushAll() {
    pushNewReceived()
    pushNewSent()
  }

  def pushNewReceived (){
    val newReceivedSmses = smsDb.findAllRecievedSinceLastSync(this)
    info("Detected [%d] new received sms messages, will push them now.", newReceivedSmses.size)
    inFuture { newReceivedSmses foreach(smsPusher.pushRecievedSms) }
  }

  def pushNewSent() {
    val newSetSmses = smsDb.findAllSentSinceLastSync(this)
    info("Detected [%d] new sent sms messages, will push them now.", newSetSmses.size)
    inFuture { newSetSmses foreach(smsPusher.pushSentSms) }
  }
}

