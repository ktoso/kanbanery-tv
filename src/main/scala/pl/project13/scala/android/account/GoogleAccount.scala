package pl.project13.scala.android.account

import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings
import android.provider.Settings.Secure

object GoogleAccount {

  def email(implicit ctx: Context): Option[String] = {
    val manager = AccountManager.get(ctx)
    val accounts = manager.getAccountsByType("com.google")

    val possibleEmails = for (account <- accounts)
      yield account.name

    possibleEmails.headOption orElse None
  }

  def deviceId(implicit ctx: Context): String = {
    val contentResolver = ctx.getContentResolver
//    Settings.System.getString(contentResolver, Settings.System.ANDROID_ID)
    Secure.getString(contentResolver, Secure.ANDROID_ID)
  }
}
