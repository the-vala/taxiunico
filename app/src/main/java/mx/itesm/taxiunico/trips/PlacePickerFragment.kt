package mx.itesm.taxiunico.trips


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_place_picker.*

import mx.itesm.taxiunico.R

class PlacePickerFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        requestMapPermission()
        initializeMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_picker, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        confirmButton.setOnClickListener {
            //TODO pass value to configuration fragment
            requireFragmentManager().popBackStack()
        }
    }


    private fun initializeMap() {
        val stationCoordinates = LatLng(arguments!!.getDouble(LAT_ARG_KEY), arguments!!.getDouble(LONG_ARG_KEY))
        val userCoordinates = LatLng(arguments!!.getDouble(LAT_ARG_KEY) + .1, arguments!!.getDouble(LONG_ARG_KEY))

        mMap.apply {
            moveCamera(CameraUpdateFactory.newLatLng(stationCoordinates))
            setMinZoomPreference(10F)

            addMarker(
                MarkerOptions()
                    .position(stationCoordinates)
                    .title("Estacion Transpais ${arguments!!.getString(CITYNAME_ARG_KEY).toLowerCase().capitalize()}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            ).showInfoWindow()


            addMarker(MarkerOptions().position(userCoordinates)).apply { isDraggable = true }
        }
    }

    private fun requestMapPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions.first() == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults.first() == PackageManager.PERMISSION_GRANTED) {

                mMap.isMyLocationEnabled = true
            }
        }
    }


    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val CITYNAME_ARG_KEY = "cityname.arg.key"
        private const val LAT_ARG_KEY = "lat.arg.key"
        private const val LONG_ARG_KEY = "long.arg.key"

        @JvmStatic
        fun newInstance(cityName: String, lat: Double, long: Double) = PlacePickerFragment().apply {
            arguments = bundleOf(
                CITYNAME_ARG_KEY to cityName,
                LAT_ARG_KEY to lat,
                LONG_ARG_KEY to long
            )
        }
    }
}
