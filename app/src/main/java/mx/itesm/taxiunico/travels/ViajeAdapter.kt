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
package mx.itesm.taxiunico.travels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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