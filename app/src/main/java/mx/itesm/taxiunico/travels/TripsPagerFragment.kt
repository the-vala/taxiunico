package mx.itesm.taxiunico.travels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_trips_pager.*

import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.trips.TripConfirmationFragment

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
        adapter.addFragment(TripConfirmationFragment(), "TEST")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}
