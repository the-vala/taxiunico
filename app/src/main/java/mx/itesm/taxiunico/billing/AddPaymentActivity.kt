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
