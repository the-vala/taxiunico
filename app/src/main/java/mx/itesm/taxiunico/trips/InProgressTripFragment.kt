package mx.itesm.taxiunico.trips


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_in_progress_trip.*

import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.util.toSentenceCase


class InProgressTripFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_in_progress_trip, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments!!.let {
            Glide
                .with(requireContext())
                .load("https://firebasestorage.googleapis.com/v0/b/taxi-unico-11f36.appspot.com/o/${it.getString(
                    MAP_URL_KEY)}?alt=media")
                .placeholder(ColorDrawable(Color.LTGRAY))
                .into(map)

            destination.text = it.getString(DESTINATION_ADDRESS_KEY)
            origin.text = it.getString(ORIGIN_ADDRESS_KEY)
            driverName.text = it.getString(DRIVER_NAME_KEY)
            distance.text = getString(R.string.distance, it.getDouble(DISTANCE_NAME_KEY) / 1000)
            time.text = getString(R.string.hours, it.getDouble(DURATION_NAME_KEY) / 60)
        }
    }

    companion object {
        fun newInstance(
            destinationAddress: String,
            originAddress: String,
            mapUrl: String,
            driverName: String,
            distance: Double,
            duration: Double
        ) = InProgressTripFragment().apply {
            arguments = bundleOf(
                DESTINATION_ADDRESS_KEY to destinationAddress,
                ORIGIN_ADDRESS_KEY to originAddress,
                MAP_URL_KEY to mapUrl,
                DRIVER_NAME_KEY to driverName.toSentenceCase(),
                DISTANCE_NAME_KEY to distance,
                DURATION_NAME_KEY to duration
            )
        }

        private const val DESTINATION_ADDRESS_KEY = "destination.key"
        private const val ORIGIN_ADDRESS_KEY = "origin.key"
        private const val MAP_URL_KEY = "map.key"
        private const val DRIVER_NAME_KEY = "driver.key"
        private const val DISTANCE_NAME_KEY = "distance.key"
        private const val DURATION_NAME_KEY = "duration.key"

    }
}
