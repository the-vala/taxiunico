package mx.itesm.taxiunico.billing

import com.google.firebase.firestore.FirebaseFirestore


class PaymentService {
    private val db = FirebaseFirestore.getInstance()

    fun getMethods(userId: String, onComplete: (MutableList<PaymentMethod.Card>) -> Unit) {
        db.collection(PAYMENT_COLLECTION_KEY)
            .document(userId).collection(METHODS_COLLECTION_KEY).get().addOnSuccessListener {
                val methods = it.documents
                    .filter { it.exists() }
                    .map { it.toObject(PaymentMethod.Card::class.java)!! }.toMutableList()
                onComplete(methods)
            }
    }

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