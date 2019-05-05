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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.auth.AuthService

class CompletedTripsFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var adapter: ViajeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var authService: AuthService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        authService = AuthService(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)
        adapter = ViajeAdapter(mutableListOf(), authService)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        MainScope().launch {
            var viajes = ViajeService().getTravelHistory(auth.uid!!)
            viajes = viajes.filter{it.second.completed}.toMutableList()
            adapter.setData(viajes)
        }
    }

}
