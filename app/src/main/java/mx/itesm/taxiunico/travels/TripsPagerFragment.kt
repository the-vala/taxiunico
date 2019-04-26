package mx.itesm.taxiunico.travels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_trips_pager.*

import mx.itesm.taxiunico.R

class TripsPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TripsPagerAdapter(fragmentManager!!)
        adapter.addFragment(CompletedTripsFragment() , "Viajes Completados")
        adapter.addFragment(PendingTripsFragment() , "Viajes Pendientes")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}
