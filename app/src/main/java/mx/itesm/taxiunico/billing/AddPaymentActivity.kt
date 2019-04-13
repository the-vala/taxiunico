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

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_add_payment.*
import mx.itesm.taxiunico.R

class AddPaymentActivity : AppCompatActivity() {

    val paymentService = PaymentService()
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)

        button.setOnClickListener {
            savePaymentMethod()
        }
    }

    private fun savePaymentMethod() {
        val cardNumText = cardNum.text.toString()
        paymentService.addMethod(auth.uid!!, PaymentMethod.Card(
            last4Digits = cardNumText.substring(cardNumText.length - 4, cardNumText.length).toInt(),
            expDate = expDateText.text.toString()
        )) { finish() }
    }
}
