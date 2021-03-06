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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_trips_pager.*
import mx.itesm.taxiunico.Network.ConnectionViewModel

import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.trips.TripConfigurationViewModel

class TripsPagerFragment : Fragment() {

    private lateinit var adapter: TripsPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trips_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            Toast.makeText(requireContext(), "Cargando viajes", Toast.LENGTH_SHORT).show()
            adapter = TripsPagerAdapter(fragmentManager!!)
            adapter.addFragment(CompletedTripsFragment(), "Viajes Completados")
            adapter.addFragment(PendingTripsFragment(), "Viajes Pendientes")
            //adapter.addFragment(TripConfirmationFragment(), "TEST")
            viewPager.adapter = adapter
            tabs.setupWithViewPager(viewPager)

    }
}
