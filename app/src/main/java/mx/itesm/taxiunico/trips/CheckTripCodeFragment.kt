package mx.itesm.taxiunico.trips


import android.os.Bundle
import android.support.v4.app.Fragment
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


    private fun verifyCode() {
        // TODO(ARIEL) verify code
        /**
         * if successful
         * fragmentManager.beginTransaction()
         * .replace(R.id.mainContent, TripConfigurationFragment.newInstance()
         * .commitAllowingStateLoss()
         *
         * else mostrar pantalla de codigo invalido
         */
    }
}
