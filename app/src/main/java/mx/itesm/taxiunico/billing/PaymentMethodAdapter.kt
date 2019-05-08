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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_payment_form_card.cardNumText
import java.nio.channels.IllegalSelectorException
import mx.itesm.taxiunico.R

/**
 * Modelo para definir metodo de pago. Define dos tipos, efectivo y tarjeta
 */
sealed class PaymentMethod {
    object Cash: PaymentMethod()
    data class Card(
        var last4Digits: Int = 0,
        var expDate: String = ""
    ): PaymentMethod()
}

/**
 * Adapter para mostrar lista de metodos de pago
 */
class PaymentMethodAdapter(
    private val paymentMethods: MutableList<PaymentMethod>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when(viewType) {
        CARD -> CardViewHolder(parent.inflate(R.layout.row_payment_form_card, false))
        CASH -> CardViewHolder(parent.inflate(R.layout.row_payment_form_cash, false))
        else -> throw Throwable("")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (paymentMethods[position] is PaymentMethod.Card) {
            (holder as CardViewHolder).bindData(paymentMethods[position] as PaymentMethod.Card)
        }
    }

    override fun getItemViewType(position: Int): Int = when(paymentMethods[position]) {
        is PaymentMethod.Card -> CARD
        is PaymentMethod.Cash -> CASH
    }

    fun setData(mutableList: MutableList<PaymentMethod>) {
        paymentMethods.clear()
        paymentMethods.addAll(mutableList)

        notifyDataSetChanged()
    }

    override fun getItemCount() = paymentMethods.size

    companion object {
        const val CARD = 0
        const val CASH = 1
    }
}

/**
 * Clase para hacer bind entre el objeto tarjeta y el viewholder
 */
class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindData(card: PaymentMethod.Card) {
        with (card) {
            view.findViewById<TextView>(R.id.cardNumText).text = view.context.getString(
                R.string.card_template, last4Digits)
            view.findViewById<TextView>(R.id.expDateText).text = expDate
        }
    }
}

/**
 * Clase para hacer bind entre el objeto cash y el viewholder
 */
class CashViewHolder(view: View) : RecyclerView.ViewHolder(view)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}