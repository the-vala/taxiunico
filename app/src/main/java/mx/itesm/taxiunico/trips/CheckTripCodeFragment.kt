package mx.itesm.taxiunico.trips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_check_trip_code.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

import mx.itesm.taxiunico.R
class CheckTripCodeFragment : Fragment() {

    private val codeService = CodeService()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_check_trip_code, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscar_button.setText("Jwzc74")
        buscar_button.setOnClickListener {
            verifyCode()
        }
    }

    private fun verifyCode() {
        MainScope().launch {
            val result = codeService.getTravelData(buscar_button.text.toString())
            when(result) {
                is Result.Success -> startTripConfiguration(result.result.origin, result.result.destination)
            }
        }
    }

    private fun startTripConfiguration(departingCityId: String, destinationCityId: String) {
        requireFragmentManager().beginTransaction()
            .replace(
                android.R.id.content,
                TripConfigurationFragment.newInstance(departingCityId, destinationCityId))
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }
}
