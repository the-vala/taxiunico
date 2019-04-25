package mx.itesm.taxiunico.trips

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.fragment_trip_configuration.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mx.itesm.taxiunico.R

class TripConfigurationFragment : Fragment() {
    private val busStationStation = BusStationService()

    private lateinit var departureStation: Station
    private lateinit var destinationStation: Station

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trip_configuration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainScope().launch {
            val stations = busStationStation.getStations()

            destinationStation = stations.find {
                it.cityId == arguments!!.getString(DESTINATION_CITY_ID)
            } ?: throw IllegalArgumentException()

            departureStation = stations.find {
                it.cityId == arguments!!.getString(DEPARTING_CITY_ID)
            } ?: throw IllegalArgumentException()


            //TODO quitar test
            testLabel.text = destinationStation.toString() + departureStation.toString()
        }
    }

    companion object {
        fun newInstance(departingCityId: String, destinationCityId: String) = TripConfigurationFragment().apply {
            arguments = bundleOf(
                DEPARTING_CITY_ID to departingCityId,
                DESTINATION_CITY_ID to destinationCityId
            )
        }

        private const val DEPARTING_CITY_ID = "departing.city.id"
        private const val DESTINATION_CITY_ID = "destination.city.id"
    }
}
