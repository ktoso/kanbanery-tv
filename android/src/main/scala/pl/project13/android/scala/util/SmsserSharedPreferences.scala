package pl.project13.android.scala.util

import java.util.Date
import android.content.{SharedPreferences, Context}
import java.util
import pl.project13.smsser.android.R

trait SmsserSharedPreferences {

  import SmsserSharedPreferences._

  implicit def ctx: Context

  lazy val sharedPreferences = ctx.getSharedPreferences("SmsserSharedPreferences", 0)

  def sharedPreferencesPhoneNumber_=(number: String) {
    withSharedPreferencesEditor { _.putString(PhoneNumberKey, number) }
  }
  def sharedPreferencesPhoneNumber =
    sharedPreferences.getString(PhoneNumberKey, "")


  def sharedPreferencesLastSyncedSmsDate = {
    val lastSyncedSmsTime = sharedPreferences.getLong(LastSyncedSmsDate, 0)
    new util.Date(lastSyncedSmsTime)
  }

  def sharedPreferencesLastSyncedSmsDate_=(date: util.Date) {
    withSharedPreferencesEditor { _.putLong(LastSyncedSmsDate, date.getTime) }
  }

  /** Updates LastSyncedSmsDate if the given date is "after" the date */
  def sharedPreferencesLastSyncedSmsDate_<<(date: util.Date) {
    if(sharedPreferencesLastSyncedSmsDate.getTime < date.getTime) {
      sharedPreferencesLastSyncedSmsDate = date
    }
  }


  def withSharedPreferencesEditor(block: SharedPreferences.Editor => Unit) {
    val editor = sharedPreferences.edit()
    block(editor)
    editor.commit()
  }
}

object SmsserSharedPreferences {
  def PhoneNumberKey()(implicit ctx: Context)= ctx.getString(R.string.preference_key_phone_number)
  def LastSyncedSmsDate()(implicit ctx: Context) = ctx.getString(R.string.preference_key_last_synced_date)

  def onContext(context: Context) =
    new SmsserSharedPreferences {
      val ctx = context
    }
}
