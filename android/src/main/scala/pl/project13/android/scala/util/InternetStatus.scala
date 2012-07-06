package pl.project13.android.scala.util

import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author Konrad Malawski
 */
class InternetStatus(connectivityManager: ConnectivityManager) {
  val NETWORK_NAME_MOBILE: String = "MOBILE"
  val NETWORK_NAME_WIFI: String = "WIFI"

  def isOnWifi: Boolean = isConnectedTo(NETWORK_NAME_WIFI)

  def isOnMobileNetwork: Boolean = isConnectedTo(NETWORK_NAME_MOBILE)

  private def isConnectedTo(networkName: String): Boolean = {
    val activeNetworkInfo: NetworkInfo = connectivityManager.getActiveNetworkInfo
    val connectedOrConnecting: Boolean = activeNetworkInfo.isConnectedOrConnecting
    connectedOrConnecting && (networkName == activeNetworkInfo.getTypeName)
  }

  def isOnline: Boolean = {
    var weAreOnline: Boolean = false
    try {
      val activeNetworkInfo: NetworkInfo = connectivityManager.getActiveNetworkInfo
      weAreOnline = activeNetworkInfo.isConnectedOrConnecting
    }
    catch {
      case e: Exception => {
        return false
      }
    }
    weAreOnline
  }

  def isOffline: Boolean = !isOnline

}