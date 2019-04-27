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

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
