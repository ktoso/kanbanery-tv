package pl.project13.smsser.android.activity

import pl.project13.smsser.android.{R, TR}
import pl.project13.android.scala.toast.ScalaToasts
import pl.project13.android.scala.activity.{ScalaPreferenceActivity, ContentView, ImplicitContext, ScalaActivity}
import android.os.{Handler, Bundle}
import android.graphics.drawable.Drawable
import pl.project13.smsser.android.backend.{BackendConfig, SmsserBackendClient}
import pl.project13.android.scala.account.GoogleAccount
import pl.project13.smsser.android.sms.{SimpleAndroidSms, SmsPusher, SmsDb}
import pl.project13.smsser.proto.gen.Sms.DeviceIs
import pl.project13.smsser.proto.gen.Response
import java.util.concurrent.Executors
import pl.project13.android.scala.util.{InternetStatus, ThisPhone}
import android.content.Context
import android.net.ConnectivityManager

class SettingsActivity extends ScalaPreferenceActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

   addPreferencesFromResource(R.xml.preferences)
  }
}
