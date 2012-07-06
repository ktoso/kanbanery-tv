package pl.project13.smsser.android.sms

import android.telephony.SmsManager

class SmsSender(smsManager: SmsManager) {

  def send(to: String, message: String) {
    smsManager.sendTextMessage(to, null, message, null, null)
  }
}

object SmsSender extends SmsSender(SmsManager.getDefault)
