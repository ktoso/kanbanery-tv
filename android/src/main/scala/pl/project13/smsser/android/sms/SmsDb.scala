package pl.project13.smsser.android.sms

import android.net.Uri
import android.content.Context
import collection.mutable
import android.util.Log
import pl.project13.android.scala.util.{Logging, SmsserSharedPreferences}
import java.text.SimpleDateFormat

class SmsDb private()  {

  val Tag = getClass.getSimpleName

  import SmsDb._

  def findAll(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllIn(SmsAllUri)
  }

  def findAllSinceLastSync(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllInSinceLastSync(SmsAllUri)
  }

  def findAllSent(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllIn(SmsOutboxUri).filter(_.messageType == SimpleAndroidSmsType.Outgoing)
  }

  def findAllSentSinceLastSync(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllInSinceLastSync(SmsOutboxUri).filter(_.messageType == SimpleAndroidSmsType.Outgoing)
  }

  def findAllRecieved(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllIn(SmsInboxUri).filter(_.messageType == SimpleAndroidSmsType.Incoming)
  }

  def findAllRecievedSinceLastSync(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllInSinceLastSync(SmsInboxUri).filter(_.messageType == SimpleAndroidSmsType.Incoming)
  }

  private def findAllInSinceLastSync(smsContentUri: Uri)(implicit ctx: Context): List[SimpleAndroidSms] = {
    val preferences = SmsserSharedPreferences.onContext(ctx)

    val smsDate = preferences.sharedPreferencesLastSyncedSmsDate
    Log.d(Tag, "Will query for sms messages after: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(smsDate))

    findAllIn(smsContentUri, "date > ?", Array(smsDate.getTime.toString))
  }

  private def findAllIn(smsContentUri: Uri)(implicit ctx: Context): List[SimpleAndroidSms] = {
    findAllIn(smsContentUri, null, null)
  }

  private def findAllIn(smsContentUri: Uri, selection: String, selectionArgs: Array[String])(implicit ctx: Context): List[SimpleAndroidSms] = {
    val selectionLogging = if (selection != null && selectionArgs != null) {
      " with selection : [" + selection + " @ " + selectionArgs.mkString(",") + "]"
    } else {
      ""
    }
    Log.d(Tag, "Trying to read sms messages from: " + smsContentUri + selectionLogging)

    val cursor = ctx.getContentResolver.query(smsContentUri, null, selection, selectionArgs, null)
    Log.d(Tag, "Found ["+cursor.getCount+"] messages, will map them to SimpleAndroidSms...")

    val smsses = mutable.ListBuffer[SimpleAndroidSms]()
    while(cursor.moveToNext()) {
      var msgData = Map[String, String]();
      var idx = 0
      while (idx < cursor.getColumnCount) {
        msgData += cursor.getColumnName(idx) -> cursor.getString(idx)
        idx += 1
      }

      smsses += SimpleAndroidSms.fromMap(msgData)
    }

    Log.d(Tag, "Found " + smsses.size + " sms messages in: " + smsContentUri)
    smsses.toList.sortBy(_.date.getTime)
  }
}

object SmsDb extends SmsDb {
//  val SmsInboxUri = Uri.parse("content://sms/inbox")
//  val SmsOutboxUri = Uri.parse("content://sms/sent")
  val SmsAllUri = Uri.parse("content://sms")

  val SmsInboxUri = Uri.parse("content://sms")
  val SmsOutboxUri = Uri.parse("content://sms")
}
