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
package mx.itesm.taxiunico.trips

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_trip_configuration.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.auth.AuthService
import mx.itesm.taxiunico.models.FreshTrip
import mx.itesm.taxiunico.models.Station
import mx.itesm.taxiunico.services.BusStationService
import mx.itesm.taxiunico.services.TripService
import mx.itesm.taxiunico.util.Event
import mx.itesm.taxiunico.util.toGeoPoint
import mx.itesm.taxiunico.util.toLatLng
import mx.itesm.taxiunico.util.toSentenceCase
import java.text.SimpleDateFormat


@Parcelize
data class TripForm(
    val firstLegPickupLocation: LatLng?= null,
    val firstLegDropoffLocation: LatLng? = null,
    val secondLegPickupLocation: LatLng? = null,
    val secondLegDropoffLocation: LatLng? = null,

    val needsFirstLegToTerminalTaxi: Boolean = false,
    val needsFirstLegToDestinationTaxi: Boolean = false,
    val needsSecondLegToTerminalTaxi: Boolean = false,
    val needsSecondLegToHomeTaxi: Boolean = false
) : Parcelable

class TripConfigurationFragment : Fragment() {
    private val busStationStation = BusStationService()

    private lateinit var homeBusStation: Station
    private lateinit var destinationBusStation: Station

    private var tripForm = TripForm()

    private lateinit var vm: TripConfigurationViewModel
    private var placePickerChangedObserver: Observer<Event<LatLng>>? = null

    private var currentJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(requireActivity()).get(TripConfigurationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trip_configuration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentJob = fetchLocationDataAndRender()

        firstLegToTerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsFirstLegToTerminalTaxi = isChecked)
        }

        firstLegToDestination.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsFirstLegToDestinationTaxi = isChecked)
        }


        secondLegtoTerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsSecondLegToTerminalTaxi = isChecked)
        }

        secondLegToHome.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsSecondLegToHomeTaxi = isChecked)
        }


        if (!arguments!!.getBoolean(ROUND_TRIP)) {
            linearLayout2.visibility = View.GONE
        }

        firstLegPickupAddress.setOnClickListener { openFirstLegPickupAddress() }
        firstLegDropoffAddress.setOnClickListener { openFirstLegDropoffAddress() }
        secondLegPickupAddress.setOnClickListener { openSecondLegPickupAddress() }
        secondLegDropoffAddress.setOnClickListener { openSecondLegDropoffAddress() }
        confirmButton.setOnClickListener { saveTrips() }
    }

    private fun saveTrips() {
        MainScope().launch {
            val trips = mutableListOf<FreshTrip>()
            val userId = AuthService(requireContext()).getUserUid()!!

            val firstLegDepartureDate = arguments!!.getString(FIRST_LEG_DEPARTURE_DATE)
            val secondLegDepartureDate = arguments!!.getString(SECOND_LEG_DEPARTURE_DATE, null)

            if (tripForm.needsFirstLegToTerminalTaxi) {
                trips.add(FreshTrip(
                    userId = userId,
                    dateTime = SimpleDateFormat("yyyy-MM-dd").parse("2019-03-01"),
                    origin = tripForm.firstLegPickupLocation!!.toGeoPoint(),
                    destination = homeBusStation.cord!!
                ))
            }

            if (tripForm.needsFirstLegToDestinationTaxi) {
                trips.add(FreshTrip(
                    userId = userId,
                    dateTime = SimpleDateFormat("yyyy-MM-dd").parse("2019-03-01"),
                    origin = destinationBusStation.cord!!,
                    destination = tripForm.firstLegDropoffLocation!!.toGeoPoint()
                ))
            }


            if (tripForm.needsSecondLegToTerminalTaxi) {
                trips.add(FreshTrip(
                    userId = userId,
                    dateTime = SimpleDateFormat("yyyy-MM-dd").parse("2019-03-01"),
                    origin = tripForm.secondLegPickupLocation!!.toGeoPoint(),
                    destination = destinationBusStation.cord!!
                ))
            }


            if (tripForm.needsSecondLegToHomeTaxi) {
                trips.add(FreshTrip(
                    userId = userId,
                    dateTime = SimpleDateFormat("yyyy-MM-dd").parse("2019-03-01"),
                    origin = homeBusStation.cord!!,
                    destination = tripForm.secondLegDropoffLocation!!.toGeoPoint()
                ))
            }

            TripService().addTrips(trips)
        }
    }


    private fun openFirstLegPickupAddress() {
        requireFragmentManager().beginTransaction()
            .replace(android.R.id.content, PlacePickerFragment.newInstance(
                referencePointLabel = getString(R.string.central, homeBusStation.city.toSentenceCase()),
                referencePointLocation = LatLng(homeBusStation.cord!!.latitude, homeBusStation.cord!!.longitude),
                selectedPointLocation = tripForm.firstLegPickupLocation
            ))
            .addToBackStack(null)
            .commitAllowingStateLoss()

        placePickerChangedObserver = Observer { event ->
            event.getContentIfNotHandled()?.let {
                tripForm = tripForm.copy(firstLegPickupLocation = it)
                vm.getLocation().removeObserver(placePickerChangedObserver!!)
                placePickerChangedObserver = null
            }
        }

        vm.getLocation().observe(this, placePickerChangedObserver!!)
    }

    private fun openFirstLegDropoffAddress() {
        requireFragmentManager().beginTransaction()
            .replace(android.R.id.content, PlacePickerFragment.newInstance(
                referencePointLabel = getString(R.string.central, destinationBusStation.city.toSentenceCase()),
                referencePointLocation = destinationBusStation.cord!!.toLatLng(),
                selectedPointLocation = tripForm.firstLegDropoffLocation
            ))
            .addToBackStack(null)
            .commitAllowingStateLoss()

        placePickerChangedObserver = Observer { event ->
            event.getContentIfNotHandled()?.let {
                tripForm = tripForm.copy(firstLegDropoffLocation = it)
                vm.getLocation().removeObserver(placePickerChangedObserver!!)
                placePickerChangedObserver = null
            }
        }

        vm.getLocation().observe(this, placePickerChangedObserver!!)
    }

    private fun openSecondLegPickupAddress() {
        requireFragmentManager().beginTransaction()
            .replace(android.R.id.content, PlacePickerFragment.newInstance(
                referencePointLabel = getString(R.string.central, destinationBusStation.city.toSentenceCase()),
                referencePointLocation = destinationBusStation.cord!!.toLatLng(),
                selectedPointLocation = tripForm.secondLegPickupLocation
            ))
            .addToBackStack(null)
            .commitAllowingStateLoss()

        placePickerChangedObserver = Observer { event ->
            event.getContentIfNotHandled()?.let {
                tripForm = tripForm.copy(secondLegPickupLocation = it)
                vm.getLocation().removeObserver(placePickerChangedObserver!!)
                placePickerChangedObserver = null
            }
        }

        vm.getLocation().observe(this, placePickerChangedObserver!!)
    }

    private fun openSecondLegDropoffAddress() {
        requireFragmentManager().beginTransaction()
            .replace(android.R.id.content, PlacePickerFragment.newInstance(
                referencePointLabel = getString(R.string.central, homeBusStation.city.toSentenceCase()),
                referencePointLocation = homeBusStation.cord!!.toLatLng(),
                selectedPointLocation = tripForm.secondLegDropoffLocation
            ))
            .addToBackStack(null)
            .commitAllowingStateLoss()

        placePickerChangedObserver = Observer { event ->
            event.getContentIfNotHandled()?.let {
                tripForm = tripForm.copy(secondLegDropoffLocation = it)
                vm.getLocation().removeObserver(placePickerChangedObserver!!)
                placePickerChangedObserver = null
            }
        }

        vm.getLocation().observe(this, placePickerChangedObserver!!)
    }

    private fun fetchLocationDataAndRender() = MainScope().launch {
        val stations = busStationStation.getStations()

        destinationBusStation = stations.find {
            it.cityId == arguments!!.getString(DESTINATION_CITY_ID)
        } ?: throw IllegalArgumentException()

        homeBusStation = stations.find {
            it.cityId == arguments!!.getString(HOME_CITY_ID)
        } ?: throw IllegalArgumentException()

        render()
    }

    override fun onResume() {
        super.onResume()

        // Re-attach the place picker observer, if any.
        placePickerChangedObserver?.let { vm.getLocation().observe(this, it) }
    }

    override fun onPause() {
        super.onPause()
        currentJob?.cancel()
    }

    /**
     * Renders the content
     */
    private fun render() {
        firstLegTitle.text = getString(
            R.string.from_to,
            homeBusStation.city.toSentenceCase(),
            destinationBusStation.city.toSentenceCase())

        secondLegTitle.text = getString(
            R.string.from_to,
            destinationBusStation.city.toSentenceCase(),
            homeBusStation.city.toSentenceCase())
    }

    companion object {
        fun newInstance(
            /**
             * The ID of the city where the bus takes off.
             */
            homeCityId: String,
            /**
             * The ID of the city where the bus is arriving.
             */
            destinationCityId: String,
            /**
             * Specifies if the user needs a bus to return home.
             */
            isRoundTrip: Boolean,
            /**
             * The date when the first bus leaves.
             */
            firstLegDepartureDate: String,
            /**
             * The date when the second bus leaves. Is null when the [isRoundTrip] is false.
             */
            secondLegDepartureDate: String?
        ) =
            TripConfigurationFragment().apply {
                arguments = bundleOf(
                    HOME_CITY_ID to homeCityId,
                    DESTINATION_CITY_ID to destinationCityId,
                    ROUND_TRIP to isRoundTrip,
                    FIRST_LEG_DEPARTURE_DATE to firstLegDepartureDate,
                    SECOND_LEG_DEPARTURE_DATE to secondLegDepartureDate

                )
            }

        private const val HOME_CITY_ID = "home.city.id"
        private const val DESTINATION_CITY_ID = "destination.city.id"
        private const val ROUND_TRIP = "is.round.trip.id"
        private const val FIRST_LEG_DEPARTURE_DATE= "first.leg.departure.date.id"
        private const val SECOND_LEG_DEPARTURE_DATE= "second.leg.departure.date.id"
    }
}
