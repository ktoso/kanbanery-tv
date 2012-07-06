package pl.project13.android.scala.util

import pl.project13.android.scala.account.GoogleAccount
import android.telephony.TelephonyManager
import android.app.AlertDialog
import pl.project13.smsser.android.R
import android.content.Context
import com.google.common.util.concurrent.SettableFuture
import android.text.InputType
import android.widget.{Toast, EditText}
import pl.project13.android.scala.thread.ThreadingHelpers
import android.os.Handler
import pl.project13.smsser.proto.gen.Sms.DeviceIdentity
import java.util.concurrent.Executors

class ThisPhone(implicit val ctx: Context)
  extends ViewListenerConversions
  with Logging
  with SmsserSharedPreferences
  with ThreadingHelpers {

  lazy val number: SettableFuture[String] = {
    val telephonyManager = ctx.getSystemService(Context.TELEPHONY_SERVICE).asInstanceOf[TelephonyManager]
    telephonyManager.getLine1Number match {
      case nr if nr == null || nr.length == 0 =>
        askUserForHisNumber()
      case nr =>
        val future: SettableFuture[String] = SettableFuture.create()
        future.set(nr)
        future
    }
  }

  def device(implicit ctx: Context): DeviceIdentity = {
    DeviceIdentity.newBuilder
      .setId(GoogleAccount.deviceId)
      .setAccount(GoogleAccount.email.getOrElse(""))
      .build()
  }

  def askUserForHisNumber(): SettableFuture[String] = sharedPreferencesPhoneNumber match {
    case "" =>
      info("Asking manually for phone number...")
      val futureNumber: SettableFuture[String] = SettableFuture.create()

      val handler = new Handler

      val input = new EditText(ctx)
      input.setText("+")
      input.setHint("+48555222111")
      input.setSingleLine()
      input.setInputType(InputType.TYPE_CLASS_PHONE)

      val builder = new AlertDialog.Builder(ctx)
      builder.setView(input)
      builder.setPositiveButton(ctx.getString(R.string.done), () => {
        val num = input.getText.toString
        if(num.matches("\\+\\d+")) {
          sharedPreferencesPhoneNumber = num
//          futureNumber.set(num)

          // todo that's a hack...
          System.runFinalizersOnExit(true)
          System.exit(0)

        } else {
          handler post { Toast.makeText(ctx, "Asking again, that's not a valid number!", Toast.LENGTH_SHORT).show() }
          val newFuture = askUserForHisNumber()
          newFuture.addListener(() => futureNumber.set(newFuture.get), Executors.newSingleThreadExecutor())
        }
      })
      builder.setCancelable(false)
      builder.setTitle(ctx.getString(R.string.enter_your_number))
      builder.setMessage(ctx.getString(R.string.please_type_in_your_number))
      builder.show()

      futureNumber
    case num =>
      info("Phone number was already stored in shared preferences: " + num)
      val future: SettableFuture[String] = SettableFuture.create()
      future.set(num)
      future
  }
}
