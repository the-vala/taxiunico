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
package mx.itesm.taxiunico.services

import com.google.firebase.firestore.FirebaseFirestore
import mx.itesm.taxiunico.billing.PaymentMethod

/**
 * Servicio que administra las formas de pago del sistema
 */
class PaymentService {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Función que recupera la lista de formas de pago registradas al usuario actual
     */
    fun getMethods(userId: String, onComplete: (MutableList<PaymentMethod.Card>) -> Unit) {
        db.collection(PAYMENT_COLLECTION_KEY)
            .document(userId).collection(METHODS_COLLECTION_KEY).get().addOnSuccessListener {
                val methods = it.documents
                    .filter { it.exists() }
                    .map { it.toObject(PaymentMethod.Card::class.java)!! }.toMutableList()
                onComplete(methods)
            }
    }

    /**
     * Función que agrega una forma de pago a la base de datos
     */
    fun addMethod(userId: String, method: PaymentMethod.Card, onSuccess: () -> Unit) {
        db.collection(PAYMENT_COLLECTION_KEY)
            .document(userId).collection(METHODS_COLLECTION_KEY).add(method)
            .addOnSuccessListener { onSuccess() }
    }

    companion object {
        const val PAYMENT_COLLECTION_KEY = "payments"
        const val METHODS_COLLECTION_KEY = "methods"
    }
}