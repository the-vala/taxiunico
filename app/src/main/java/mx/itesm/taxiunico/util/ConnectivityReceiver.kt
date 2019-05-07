package mx.itesm.taxiunico.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (connectivityListener != null) {
            connectivityListener!!.onNetworkChanged(isConnectedOrConnecting(context))
        }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    interface ConnectivityListener {
        fun onNetworkChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityListener: ConnectivityListener? = null
    }
}