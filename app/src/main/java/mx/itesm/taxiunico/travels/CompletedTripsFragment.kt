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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.Network.ConnectionViewModel
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.services.TripService

/**
 * Fragmento para la lista de viajes pasados
 */
class CompletedTripsFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var adapter: ViajeAdapter
    private lateinit var authService: AuthService
    private lateinit var connectionVM: ConnectionViewModel
    private lateinit var tripService: TripService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionVM = ViewModelProviders.of(requireActivity()).get(ConnectionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed_trips, container, false)
    }
    
    /**
     * Función que carga las instancias necesarias para este fragmento
     */
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authService = AuthService(requireContext())
        tripService = TripService()

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        adapter = ViajeAdapter(mutableListOf(), authService)
        recyclerView.adapter = adapter
    }

    /**
     * Función que carga los viajes de firebase y los muestra en forma de lista
     */
    @FlowPreview
    override fun onResume() {
        super.onResume()
            when(authService.getUserType()) {
                UserType.DRIVER -> {
                    if (!connectionVM.getConnectionState().value!!) {
                        Toast.makeText(requireContext(),"No hay conexion.",Toast.LENGTH_SHORT).show()
                    } else {
                        MainScope().launch {
                            tripService.getRealTimeDriverCompletedHistory(auth.uid!!).collect { adapter.setData(it) }
                        }
                    }
                }
                UserType.TRAVELER -> {
                    if (!connectionVM.getConnectionState().value!!) {
                        Toast.makeText(requireContext(),"No hay conexion.",Toast.LENGTH_SHORT).show()
                    } else {
                        MainScope().launch {
                            tripService.getRealTimeTravelerCompletedHistory(auth.uid!!).collect { adapter.setData(it) }
                        }
                    }
                }
            }
        }
}