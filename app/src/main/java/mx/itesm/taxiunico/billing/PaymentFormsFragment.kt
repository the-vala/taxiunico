package mx.itesm.taxiunico.billing

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_payment_forms.*
import mx.itesm.taxiunico.R

class PaymentFormsFragment : Fragment() {

    private val paymentService = PaymentService()
    private val auth = FirebaseAuth.getInstance()
    private  val adapter = PaymentMethodAdapter(mutableListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_payment_forms, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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



    fun getAddPaymentIntent(): Intent = Intent(requireContext(), AddPaymentActivity::class.java)
}
