package mx.itesm.taxiunico.travels

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.Viaje
import java.io.IOException

class ViajeAdapter(private val list:MutableList<Viaje>): RecyclerView.Adapter<ViajeAdapter.ViewHolder>() {

    var onItemClick: ((Viaje) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.trip_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViajeAdapter.ViewHolder, pos: Int) {
        holder.bindItems(list[pos])
    }

    fun setData(mutableList: MutableList<Viaje>) {
        list.clear()
        list.addAll(mutableList)

        notifyDataSetChanged()
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bindItems(data:Viaje) {

            val ori:TextView = itemView.findViewById(R.id.ori)
            val des:TextView = itemView.findViewById(R.id.des)
            val fecha:TextView = itemView.findViewById(R.id.fecha)
            val vehiculo: TextView = itemView.findViewById(R.id.vehiculo)
            val costo:TextView = itemView.findViewById(R.id.costo)
            val formaPago:TextView = itemView.findViewById(R.id.formaPago)

            var geocodeMatchesOri: List<Address>? = null
            var geocodeMatchesDes: List<Address>? = null
            val AddressOri: String?
            val AddressDes: String?

            try {
                geocodeMatchesOri = Geocoder(itemView.context)
                    .getFromLocation(data.origin.latitude,
                                     data.origin.longitude,
                                     1)
                geocodeMatchesDes = Geocoder(itemView.context)
                    .getFromLocation(data.destination.latitude,
                                     data.destination.longitude,
                            1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (geocodeMatchesOri != null) {
                AddressOri = geocodeMatchesOri[0].getAddressLine(0)
                ori.text = AddressOri.toString()
            }

            if (geocodeMatchesDes != null) {
                AddressDes = geocodeMatchesDes[0].getAddressLine(0)
                des.text = AddressDes.toString()
            }

            fecha.text = data.dateTime.toDate().toString()
            vehiculo.text = data.vehicle
            costo.text = "MX $ ${data.cost.toString()}"
            formaPago.text = data.payment
        }
    }
}