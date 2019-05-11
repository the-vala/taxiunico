/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.itesm.taxiunico.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
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