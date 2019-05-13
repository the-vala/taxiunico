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
package mx.itesm.taxiunico.auth

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import mx.itesm.taxiunico.Network.ConnectionViewModel
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.util.ConnectivityReceiver

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(),
    ConnectivityReceiver.ConnectivityListener {

    private var snackBar: Snackbar? = null
    private var receiver: BroadcastReceiver? = null
    private lateinit var connectionVM: ConnectionViewModel

    /**
     * Registra el connectivity receiver
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionVM = ViewModelProviders.of(this).get(ConnectionViewModel::class.java)

    }

    /**
     * Muestra el snackBar en caso de no conexion
     * */
    @SuppressLint("WrongConstant")
    private fun showConnectionMessage(isConnected: Boolean) {
        if (!isConnected) {
            val message = "No hay conexión"
            snackBar = Snackbar.make(findViewById(R.id.snackbar_container), message, Snackbar.LENGTH_LONG)
            snackBar?.duration = Snackbar.LENGTH_INDEFINITE
            val view = snackBar!!.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            view.layoutParams = params
            snackBar?.show()
        } else {
            snackBar?.dismiss()
        }
    }

    /**
     * instancia el listener
     * */
    override fun onResume() {
        super.onResume()

        receiver = ConnectivityReceiver()
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        ConnectivityReceiver.connectivityListener = this
        receiver = ConnectivityReceiver()
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    /**
     * anula el registro del receiver
     * */
    override fun onPause() {
        super.onPause()

        try {
            unregisterReceiver(receiver)
        } catch (err: Throwable) {
            Log.e("BaseActivity", err.stackTrace.toString())
        }
    }

    /**
     * En caso de cambio de estatus de red muestra el mensaje y
     *  cambia el valor del view model
     * */
    override fun onNetworkChanged(isConnected: Boolean) {
        showConnectionMessage(isConnected)
        connectionVM.setConnectionState(isConnected)
    }
}