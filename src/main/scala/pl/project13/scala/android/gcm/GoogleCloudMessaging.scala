package pl.project13.scala.android.gcm

import com.google.android.gcm.GCMRegistrar
import pl.project13.scala.android.util.Logging
import android.os.Bundle
import android.app.Activity

trait GoogleCloudMessaging extends Activity {
  this: Logging =>

  val SenderId = "852156741377"

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)

    GCMRegistrar.checkDevice(this)
    GCMRegistrar.checkManifest(this)
    val regId = GCMRegistrar.getRegistrationId(this)

    if (regId == "") {
      GCMRegistrar.register(this, SenderId)
    } else {
      debug("Already registered")
    }
  }

}
