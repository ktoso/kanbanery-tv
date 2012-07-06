package pl.project13.scala.android.util

import android.content.Intent
import android.telephony.SmsMessage

class SmsExtractor {
  def extractMessages(intent: Intent): List[SmsMessage] = {
    val bundle = intent.getExtras

    if (bundle != null) {
      val pdus = bundle.get("pdus").asInstanceOf[Array[Object]].toList

      pdus.map {
        pdu => SmsMessage.createFromPdu(pdu.asInstanceOf[Array[Byte]])
      }
    } else {
      Nil
    }
  }
}
