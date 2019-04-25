package mx.itesm.taxiunico.travels

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.Viaje

class ViajeAdapter(var list:ArrayList<Viaje>): RecyclerView.Adapter<ViajeAdapter.ViewHolder>() {
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

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        fun bindItems(data:Viaje) {
            val fecha:TextView = itemView.findViewById(R.id.fecha)
            val vehiculo: TextView = itemView.findViewById(R.id.vehiculo)
            val costo:TextView = itemView.findViewById(R.id.costo)
            val formaPago:TextView = itemView.findViewById(R.id.formaPago)

            fecha.text = data.dateTime
            vehiculo.text = data.vehicle
            costo.text = "MX $"+data.cost.toString()
            formaPago.text = data.payment
        }
    }
}