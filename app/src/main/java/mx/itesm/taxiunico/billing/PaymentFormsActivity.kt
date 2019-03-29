package mx.itesm.taxiunico.billing

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_payment_forms.*
import mx.itesm.taxiunico.R

class PaymentFormsActivity : AppCompatActivity() {

    private val paymentService = PaymentService()
    private val auth = FirebaseAuth.getInstance()
    private  val adapter = PaymentMethodAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_forms)

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter
        button.setOnClickListener {
            startActivity(getAddPaymentIntent())
        }
    }



    override fun onResume() {
        super.onResume()

        paymentService.getMethods(
            auth.uid!!,
            onComplete = ::updatePaymentCards
        )
    }

    private fun updatePaymentCards(newList: MutableList<PaymentMethod.Card>) {
        val list = mutableListOf<PaymentMethod>()
        list.addAll(newList)
        adapter.setData(list)
    }



    fun getAddPaymentIntent(): Intent = Intent(this, AddPaymentActivity::class.java)
}
