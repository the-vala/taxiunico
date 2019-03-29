package mx.itesm.taxiunico.billing

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.row_payment_form_card.cardNumText
import java.nio.channels.IllegalSelectorException
import mx.itesm.taxiunico.R

sealed class PaymentMethod {
    object Cash: PaymentMethod()
    data class Card(
        val last4Digits: Int,
        val expDate: String
    ): PaymentMethod()
}

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


class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindData(card: PaymentMethod.Card) {
        with (card) {
            view.findViewById<TextView>(R.id.cardNumText).text = view.context.getString(
                R.string.card_template, last4Digits)
            view.findViewById<TextView>(R.id.expDateText).text = expDate
        }
    }
}

class CashViewHolder(view: View) : RecyclerView.ViewHolder(view)

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}