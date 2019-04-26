package mx.itesm.taxiunico.travels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.Viaje

class PendingTripsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(view.context, RecyclerView.VERTICAL,false)

        val viajes = ArrayList<Viaje>()
        viajes.add(
            Viaje("LHR2J5", "06/10/18 10:15 AM", "Cord", "Cord", "Alfonso",
                "Chevrolet Sonic", 3.4F, 80.45F, ".... 5248", true, "url")
        )

        val adapter = ViajeAdapter(viajes)
        recyclerView.adapter = adapter
    }
}
