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

    /**
     *  Checa el estatus de la red
     *  @return true si la esta conectado a una red o conectandose,
     *  false en caso que esta desconectado el dispositivo.
     * */
    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    /**
     * Interfaz para checar la red en caso
     * de un cambio de estatus
     */
    interface ConnectivityListener {
        fun onNetworkChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityListener: ConnectivityListener? = null
    }
}