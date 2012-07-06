package pl.project13.smsser.android.sms

object SimpleAndroidSmsType extends Enumeration {
  type SimpleAndroidSmsType = Value

  val Incoming = Value(1)
  val Outgoing = Value(2)
  val Ignore   = Value(100)

  def fromAndroidId(`type`: Int) = `type` match {
    case 1 => Incoming
    case 2 => Outgoing
    case _ => Ignore
  }
}
