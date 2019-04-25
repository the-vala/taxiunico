package mx.itesm.taxiunico.trips


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import mx.itesm.taxiunico.R

class CheckTripCodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_check_trip_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun verifyCode() {
    }

    private fun startTripConfiguration(departingCityId: String, destinationCityId: String) {
        requireFragmentManager().beginTransaction()
            .replace(
                android.R.id.content,
                TripConfigurationFragment.newInstance(departingCityId, destinationCityId))
            .commitAllowingStateLoss()
    }

}
