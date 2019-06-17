package apps.issy.com.oceankids.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Helper class to check different device statuses during runtime
 */
class DeviceStatus(private val context: Context) {

    /**
     *  Function to return the Internet connectivity status of the device
     *  @param
     *  @return Boolean
     */
    fun Internet() : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

}