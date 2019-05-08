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

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.models.TripStatus
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.util.cost
import java.io.IOException


class ViajeAdapter(private val list:MutableList<Pair<String, Viaje>>, private val authService: AuthService): RecyclerView.Adapter<ViajeAdapter.ViewHolder>() {

    var onItemClick: ((Pair<String,Viaje>) -> Unit)? = null
    private lateinit var storage:FirebaseStorage

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_trip_item,parent,false)
        storage = FirebaseStorage.getInstance()
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bindItems(list[pos].second)
    }

    fun setData(mutableList: MutableList<Pair<String,Viaje>>) {
        list.clear()
        list.addAll(mutableList)

        notifyDataSetChanged()
    }

    inner class ViewHolder(val view:View):RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }

        fun bindItems(data:Viaje) {

            val ori:TextView = itemView.findViewById(R.id.confirmationOri)
            val des:TextView = itemView.findViewById(R.id.confirmationDateTime)
            val fecha:TextView = itemView.findViewById(R.id.fecha)
            val vehiculo: TextView = itemView.findViewById(R.id.vehiculo)
            val costo:TextView = itemView.findViewById(R.id.costo)
            val formaPago:TextView = itemView.findViewById(R.id.formaPago)
            val rating:RatingBar = itemView.findViewById(R.id.tripRatingIndicator)

            var geocodeMatchesOri: List<Address>? = null
            var geocodeMatchesDes: List<Address>? = null
            val addressOri: String?
            val addressDes: String?

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
                addressOri = geocodeMatchesOri[0].getAddressLine(0)
                ori.text = addressOri.toString()
            }

            if (geocodeMatchesDes != null) {
                addressDes = geocodeMatchesDes[0].getAddressLine(0)
                des.text = addressDes.toString()
            }

            fecha.text = data.dateTime.toDate().toString()
            vehiculo.text = data.vehicle
            costo.text = view.context.getString(R.string.cost, data.cost())
            formaPago.text = data.payment

            if(data.status == TripStatus.COMPLETED) {
                if (authService.getUserType() == UserType.DRIVER)
                    rating.rating = data.userRating.toFloat()
                else
                    rating.rating = data.driverRating.toFloat()
            }
            else {
                rating.visibility = View.GONE
            }

            if(data.imageURL.isNotEmpty()) {
                storage.reference.child(data.imageURL).downloadUrl.addOnSuccessListener {
                    val imageView = itemView.findViewById<ImageView>(R.id.mapa)

                    Glide.with(view)
                        .load(it.toString())
                        .placeholder(ColorDrawable(Color.LTGRAY))
                        .into(imageView)
                }

            }
        }
    }
}