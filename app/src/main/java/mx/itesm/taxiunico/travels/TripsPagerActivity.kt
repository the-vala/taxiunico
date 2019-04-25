package mx.itesm.taxiunico.travels

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_trips_pager.*
import mx.itesm.taxiunico.R

class TripsPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips_pager)

        val adapter = TripsPagerAdapter(supportFragmentManager)
        adapter.addFragment(CompletedTripsFragment() , "Viajes Completados")
        adapter.addFragment(PendingTripsFragment() , "Viajes Pendientes")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }
}
