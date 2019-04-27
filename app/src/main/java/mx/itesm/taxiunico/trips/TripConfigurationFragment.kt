package mx.itesm.taxiunico.trips

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_trip_configuration.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R

@Parcelize
data class Coordinate(
    val long: String,
    val lat: String
) : Parcelable

@Parcelize
data class TripForm(
    val originPickupDropoffLocation: Coordinate = Coordinate("0", "0"),
    val destinationPickupDropoffLocation: Coordinate = Coordinate("0", "0"),
    val originToTerminalTrip: Boolean = false,
    val originFromTerminalTrip: Boolean = false,
    val destinationFromTerminalTrip: Boolean = false,
    val destinationToTerminalTrip: Boolean = false
) : Parcelable

class TripConfigurationFragment : Fragment() {
    private val busStationStation = BusStationService()

    private lateinit var departureStation: Station
    private lateinit var destinationStation: Station

    private var tripForm = TripForm()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trip_configuration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchLocationDataAndRender()

        firstLegToTrerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(originToTerminalTrip = isChecked)
        }

        firstLegFromTerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(originFromTerminalTrip = isChecked)
        }

        secondLegFromTerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(destinationFromTerminalTrip = isChecked)
        }

        secondLegtoTerminal.setOnCheckedChangeListener { _, isChecked ->
            tripForm = tripForm.copy(destinationToTerminalTrip = isChecked)
        }

        if (!arguments!!.getBoolean(ROUND_TRIP)) {
            firstLegFromTerminal.visibility =.GONE
        }

        firstLegAddress.setOnClickListener {
            openLocationPickerFragment(
                departureStation.city,
                departureStation.cord!!.latitude,
                departureStation.cord!!.longitude)
        }

        secondLegAddress.setOnClickListener {
            openLocationPickerFragment(
                destinationStation.city,
                destinationStation.cord!!.latitude,
                destinationStation.cord!!.longitude)
        }
    }

    private fun openLocationPickerFragment(cityName: String, lat: Double, long: Double) {
        requireFragmentManager().beginTransaction()
            .replace(android.R.id.content, PlacePickerFragment.newInstance(cityName, lat, long))
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun fetchLocationDataAndRender() = MainScope().launch {
        val stations = busStationStation.getStations()

        destinationStation = stations.find {
            it.cityId == arguments!!.getString(DESTINATION_CITY_ID)
        } ?: throw IllegalArgumentException()

        departureStation = stations.find {
            it.cityId == arguments!!.getString(DEPARTING_CITY_ID)
        } ?: throw IllegalArgumentException()

        render()
    }



    private fun render() {
        originCityName.text = "Tu viaje desde ${departureStation.city.toLowerCase().capitalize()}"
        destinationCityName.text = "Tu viaje hacia ${destinationStation.city.toLowerCase().capitalize()}"
    }

    companion object {
        fun newInstance(departingCityId: String, destinationCityId: String, isRoundTrip: Boolean) =
            TripConfigurationFragment().apply {
                arguments = bundleOf(
                    DEPARTING_CITY_ID to departingCityId,
                    DESTINATION_CITY_ID to destinationCityId,
                    ROUND_TRIP to isRoundTrip
                )
            }

        private const val DEPARTING_CITY_ID = "departing.city.id"
        private const val DESTINATION_CITY_ID = "destination.city.id"
        private const val ROUND_TRIP = "router.id"
    }
}
