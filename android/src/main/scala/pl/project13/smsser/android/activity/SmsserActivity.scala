
package pl.project13.smsser.android.activity

import pl.project13.smsser.android.TR
import pl.project13.android.scala.toast.ScalaToasts
import pl.project13.android.scala.activity.{ContentView, ImplicitContext, ScalaActivity}
import android.os.{Handler, Bundle}
import pl.project13.smsser.proto.gen.Ping.PongMessage
import android.graphics.drawable.Drawable
import pl.project13.smsser.android.backend.{BackendConfig, SmsserBackendClient}
import pl.project13.android.scala.account.GoogleAccount
import android.preference.ListPreference
import pl.project13.smsser.android.sms.{SimpleAndroidSms, SmsPusher, SmsDb}
import pl.project13.smsser.proto.gen.Sms.DeviceIs
import pl.project13.smsser.proto.gen.{Sms, Response}
import java.util.concurrent.Executors
import pl.project13.android.scala.util.{InternetStatus, ThisPhone}
import android.content.Context
import android.net.ConnectivityManager

class SmsserActivity extends ScalaActivity with ImplicitContext
  with ScalaToasts with ContentView
  with SmsserContextMenu {

  implicit val handler = new Handler

  val ContentView = TR.layout.main

  val smsDb = SmsDb

  lazy val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE).asInstanceOf[ConnectivityManager]
  lazy val internetStatus = new InternetStatus(connectivityManager)

  val phone = new ThisPhone

  lazy val backendConf = BackendConfig.loadFromResources(this)
  lazy val smsserBackendClient = new SmsserBackendClient(backendConf)
  lazy val smsPusher = new SmsPusher(smsserBackendClient)

  lazy val main = findView(TR.main)
  lazy val myNumberText = findView(TR.my_number_text_view)
  lazy val myEmailText = findView(TR.my_email_text_view)
  lazy val myDeviceIdText = findView(TR.my_device_id_text_view)
  lazy val pushAll = findView(TR.push_all_to_smsser)
  lazy val listMsgsWaitingForPush = findView(TR.list_waiting_for_push)

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setupBackground()

    phone.number.addListener({ myNumberText := phone.number.get() }, Executors.newSingleThreadExecutor())
    GoogleAccount.email map { myEmailText := _ }
    myDeviceIdText := GoogleAccount.deviceId

    pushAll onClick sendAllSmsMessagesToSmsser

    listMsgsWaitingForPush onClick {
      val recievedSinceLastSync = smsDb.findAllRecievedSinceLastSync
      "Recieved since last sync: [%s]".format(recievedSinceLastSync.map(i => "From " + i.address + ": " + i.body)).toastLong
    }
  }

  def setupBackground() {
    val backgroundIs = getResources.getAssets.open("bg.jpg")
    main.setBackgroundDrawable(Drawable.createFromStream(backgroundIs, null))
    backgroundIs.close()
  }

  def sendAllSmsMessagesToSmsser() {

    if(internetStatus.isOffline) {
      "Hey, it seems you're offline!\nConnect to the web and try again.".toastLong
      return
    }

    def pushToSmsser(messages:List[SimpleAndroidSms], dialogTitle: String, deviceIs: DeviceIs) {
      import pl.project13.android.scala.collection.MapWithProgressDialog._
      messages.foreachWithProgressDialog(dialogTitle, "Processing %d of %d messages...") { sms =>
        debug("Address: %s, body: %s", sms.address, sms.body)

        smsPusher.pushSms(sms, deviceIs) match {
          case Left(ex) => error(ex, "Unable to push sms")
          case Right(response) if response.getStatusType == Response.StatusType.OK =>
            info("Sms persisted!")
          case Right(response) =>
            info("Got response: " + response)
        }
      }
    }

    val recieved = smsDb.findAllRecieved
    info("Pushing " + recieved.size + " recieved messages to smsser...")
    pushToSmsser(recieved, "Uploading recieved sms messages...", DeviceIs.RECIEVER)

    val sent = smsDb.findAllSent
    info("Pushing " + sent.size + " sent messages to smsser...")
    pushToSmsser(sent, "Uploading sent sms messages...", DeviceIs.SENDER)
  }

}
