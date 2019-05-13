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
package mx.itesm.taxiunico.billing

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.row_payment_form_cash.*
import mx.itesm.taxiunico.Network.ConnectionViewModel
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.auth.BaseActivity
import mx.itesm.taxiunico.auth.LoginActivity
import mx.itesm.taxiunico.services.PaymentService

/**
 * Actividad para agregar una nueva forma de pago
 */
class AddPaymentActivity : BaseActivity() {
    private lateinit var connectionVM: ConnectionViewModel
    val paymentService = PaymentService()
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)

        connectionVM = ViewModelProviders.of(this).get(ConnectionViewModel::class.java)

        buttonPayForm.setOnClickListener {
            savePaymentMethod()
        }
    }

    /**
     * Función para guardar información de un nuevo método de pago
     */
    private fun savePaymentMethod() {
        var valid = true
        val cardNumText = cardNum.text.toString()
        val cvc = cvcText.text.toString()
        val date = expDateText.text.toString()
        val country = countryText.text.toString()

        if (!connectionVM.getConnectionState().value!!) {
            Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show()
            valid = false
        }

        if (TextUtils.isEmpty(cardNumText) || cardNumText.length != 16 || !isPhoneValid(cardNumText)) {
            cardNum.error = "Número inválido"
            valid = false
        } else {
            cardNum.error = null
        }

        if (TextUtils.isEmpty(cvc) || cardNumText.length != 3 || !isPhoneValid(cvc)) {
            cvcText.error = "Número inválido"
            valid = false
        } else {
            cvcText.error = null
        }

        if (TextUtils.isEmpty(date)) {
            expDateText.error = "Fecha inválida"
            valid = false
        } else {
            expDateText.error = null
        }

        if (TextUtils.isEmpty(country)) {
            countryText.error = "País inválido"
            valid = false
        } else {
            countryText.error = null
        }

        if (valid) {
            paymentService.addMethod(auth.uid!!, PaymentMethod.Card(
                last4Digits = cardNumText.substring(cardNumText.length - 4, cardNumText.length).toInt(),
                expDate = expDateText.text.toString()
            )) { finish() }
        }

    }

    private fun isPhoneValid(phone: String): Boolean {
        return Patterns.PHONE.toRegex().matches(phone)
    }
}
