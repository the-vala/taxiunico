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
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_trip_configuration.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.Codes
import mx.itesm.taxiunico.services.AuthService
import mx.itesm.taxiunico.models.FreshTrip
import mx.itesm.taxiunico.models.Station
import mx.itesm.taxiunico.services.BusStationService
import mx.itesm.taxiunico.services.TripService
import mx.itesm.taxiunico.util.Event
import mx.itesm.taxiunico.util.toGeoPoint
import mx.itesm.taxiunico.util.toLatLng
import mx.itesm.taxiunico.util.toSentenceCase

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

    private lateinit var tripCode: Codes
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
            firstLegPickupAddress.isInvisible = !isChecked
        }

        firstLegToDestination.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsFirstLegToDestinationTaxi = isChecked)
            firstLegDropoffAddress.isInvisible = !isChecked
        }


        secondLegtoTerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsSecondLegToTerminalTaxi = isChecked)
            secondLegPickupAddress.isInvisible = !isChecked
        }

        secondLegToHome.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(needsSecondLegToHomeTaxi = isChecked)
            secondLegDropoffAddress.isInvisible = !isChecked
        }

        tripCode = arguments!!.getParcelable<Codes>(CODE_ID)!!

        if (tripCode.isRound) {
            linearLayout2.visibility = View.GONE
        }

        firstLegPickupAddress.setOnClickListener { openFirstLegPickupAddress() }
        firstLegDropoffAddress.setOnClickListener { openFirstLegDropoffAddress() }
        secondLegPickupAddress.setOnClickListener { openSecondLegPickupAddress() }
        secondLegDropoffAddress.setOnClickListener { openSecondLegDropoffAddress() }
        confirmButton.setOnClickListener { saveTrips() }
    }

    private fun validateTripForm(): ValidationResult = when {
        tripForm.needsFirstLegToTerminalTaxi && tripForm.firstLegPickupLocation == null ->
            ValidationResult(false, "Especifica una ubicacion para xxxx")
        tripForm.needsFirstLegToDestinationTaxi && tripForm.firstLegDropoffLocation == null ->
            ValidationResult(false, "Especifica una ubicacion para xxxx")
        tripForm.needsSecondLegToTerminalTaxi && tripForm.secondLegPickupLocation == null ->
            ValidationResult(false, "Especifica una ubicacion para xxxx")
        tripForm.needsSecondLegToHomeTaxi && tripForm.secondLegDropoffLocation == null ->
            ValidationResult(false, "Especifica una ubicacion para xxxx")
        else -> ValidationResult(true)
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


    private fun saveTrips() {
        val trips = mutableListOf<FreshTrip>()
        val userId = AuthService(requireContext()).getUserUid()!!

        val validationResult = validateTripForm()

        if (!validationResult.valid) {
            Snackbar.make(requireView(), validationResult.message, Snackbar.LENGTH_SHORT).show()
            return
        }

        if (tripForm.needsFirstLegToTerminalTaxi) {
            trips.add(FreshTrip(
                relatedCityId = homeBusStation.cityId,
                userId = userId,
                dateTime = (tripCode.firstLegDepartureTime - Times.HOUR).toDate(),
                origin = tripForm.firstLegPickupLocation!!.toGeoPoint(),
                destination = homeBusStation.cord!!
            ))
        }

        if (tripForm.needsFirstLegToDestinationTaxi) {
            trips.add(FreshTrip(
                relatedCityId = destinationBusStation.cityId,
                userId = userId,
                dateTime = (tripCode.firstLegArrivalTime + Times.HALF_HOUR).toDate(),
                origin = destinationBusStation.cord!!,
                destination = tripForm.firstLegDropoffLocation!!.toGeoPoint()
            ))
        }

        if (tripForm.needsSecondLegToTerminalTaxi) {
            trips.add(FreshTrip(
                relatedCityId = destinationBusStation.cityId,
                userId = userId,
                dateTime = (tripCode.secondLegDepartureTime - Times.HOUR).toDate(),
                origin = tripForm.secondLegPickupLocation!!.toGeoPoint(),
                destination = destinationBusStation.cord!!
            ))
        }


        if (tripForm.needsSecondLegToHomeTaxi) {
            trips.add(FreshTrip(
                relatedCityId = homeBusStation.cityId,
                userId = userId,
                dateTime = (tripCode.secondLegArrivalTime + Times.HALF_HOUR).toDate(),
                origin = homeBusStation.cord!!,
                destination = tripForm.secondLegDropoffLocation!!.toGeoPoint()
            ))
        }
   
        MainScope().launch {
            TripService().addTrips(trips)
            requireFragmentManager().popBackStack()
        }
    }



    private fun openFirstLegPickupAddress() {
            openPlacePicker(
                referencePointLabel = getString(R.string.central, homeBusStation.city.toSentenceCase()),
                referencePointLocation = LatLng(homeBusStation.cord!!.latitude, homeBusStation.cord!!.longitude),
                selectedPointLocation = tripForm.firstLegPickupLocation,
                onPlacePicked = { tripForm = tripForm.copy(firstLegPickupLocation = it) }
            )
    }

    private fun openFirstLegDropoffAddress() {
        openPlacePicker(
            referencePointLabel = getString(R.string.central, destinationBusStation.city.toSentenceCase()),
            referencePointLocation = destinationBusStation.cord!!.toLatLng(),
            selectedPointLocation = tripForm.firstLegDropoffLocation,
            onPlacePicked = { tripForm = tripForm.copy(firstLegDropoffLocation = it) }
        )
    }

    private fun openSecondLegPickupAddress() {
        openPlacePicker(
            referencePointLabel = getString(R.string.central, destinationBusStation.city.toSentenceCase()),
            referencePointLocation = destinationBusStation.cord!!.toLatLng(),
            selectedPointLocation = tripForm.secondLegPickupLocation,
            onPlacePicked = { tripForm = tripForm.copy(secondLegPickupLocation = it) }
        )
    }

    private fun openSecondLegDropoffAddress() {
        openPlacePicker(
            referencePointLabel = getString(R.string.central, homeBusStation.city.toSentenceCase()),
            referencePointLocation = homeBusStation.cord!!.toLatLng(),
            selectedPointLocation = tripForm.secondLegDropoffLocation,
            onPlacePicked = { tripForm = tripForm.copy(secondLegDropoffLocation = it) }
            )
    }

    private fun openPlacePicker(
        referencePointLabel: String,
        referencePointLocation: LatLng,
        selectedPointLocation: LatLng? = null,
        onPlacePicked:  (LatLng) -> Unit
    ) {
        requireFragmentManager().beginTransaction()
            .replace(android.R.id.content, PlacePickerFragment.newInstance(
                referencePointLabel = referencePointLabel,
                referencePointLocation = referencePointLocation,
                selectedPointLocation = selectedPointLocation
            ))
            .addToBackStack(null)
            .commitAllowingStateLoss()

        placePickerChangedObserver = Observer { event ->
            event.getContentIfNotHandled()?.let {
                onPlacePicked(it)
                vm.getLocation().removeObserver(placePickerChangedObserver!!)
                placePickerChangedObserver = null
            }
        }

        vm.getLocation().observe(this, placePickerChangedObserver!!)
    }

    private fun fetchLocationDataAndRender() = MainScope().launch {
        val stations = busStationStation.getStations()
        destinationBusStation = stations.find {
            it.cityId == tripCode.destination
        } ?: throw IllegalArgumentException()

        homeBusStation = stations.find {
            it.cityId == tripCode.origin
        } ?: throw IllegalArgumentException()

        render()
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
        fun newInstance(code: Codes) =
            TripConfigurationFragment().apply {
                arguments = bundleOf(CODE_ID to code)
            }

        private const val CODE_ID = "code.id"
    }

    data class ValidationResult(val valid: Boolean, val message: String = "")
}


operator fun Timestamp.plus(seconds: Int): Timestamp {
    return Timestamp(this.seconds + seconds, 0)
}

operator fun Timestamp.minus(seconds: Int): Timestamp {
    return Timestamp(this.seconds - seconds, 0)
}

class Times {
    companion object {
        const val HOUR = 3600
        const val HALF_HOUR = 1800

    }
}

