package mx.itesm.taxiunico.billing

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_payment_forms.*
import mx.itesm.taxiunico.R

class PaymentFormsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_forms)

        val payments = mutableListOf(
            PaymentMethod.Card(1234, "2020"),
            PaymentMethod.Card(8336, "2020"),
            PaymentMethod.Card(7861, "Asdfasdf"),
            PaymentMethod.Cash
        )

        val adapter = PaymentMethodAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = adapter
        adapter.setData(payments)

        button.setOnClickListener {
            startActivity(getAddPaymentIntent())
        }
    }


    fun getAddPaymentIntent(): Intent = Intent(this, AddPaymentActivity::class.java)
}
