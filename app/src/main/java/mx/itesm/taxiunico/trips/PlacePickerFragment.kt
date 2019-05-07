/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_place_picker.*

import mx.itesm.taxiunico.R

class PlacePickerFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var vm: TripConfigurationViewModel
    private lateinit var selectableMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(requireActivity()).get(TripConfigurationViewModel::class.java)
    }
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
            vm.setSelectedLocation(selectableMarker.position)
            requireFragmentManager().popBackStack()
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


    private fun initializeMap() {
        val referencePointLocation = arguments!!.getParcelable<LatLng>(REFERENCE_POINT_LOCATION)
        val selectedPoint = arguments!!.getParcelable(SELECTED_POINT_LOCATION) ?: getDefaultUserSelection()
        val referencePointLabel = arguments!!.getString(REFERENCE_POINT_LABEL)

        mMap.apply {
            moveCamera(CameraUpdateFactory.newLatLng(referencePointLocation))
            setMinZoomPreference(10F)

            addMarker(
                MarkerOptions()
                    .position(referencePointLocation)
                    .title(referencePointLabel)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            ).showInfoWindow()

            selectableMarker = addMarker(MarkerOptions().position(selectedPoint)).apply {
                isDraggable = true
            }


            setOnCameraMoveListener(object: GoogleMap.OnCameraMoveListener {
                override fun onCameraMove() {
                    selectableMarker.position = mMap.cameraPosition.target
                }
            })

            setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragEnd(marker: Marker) {
                    if (marker.id == selectableMarker.id) {
                        selectableMarker = marker
                    }
                }

                override fun onMarkerDragStart(p0: Marker?) {}

                override fun onMarkerDrag(p0: Marker?) {}
            })
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


    private fun getDefaultUserSelection(): LatLng {
        val referencePoint = arguments!!.getParcelable<LatLng>(REFERENCE_POINT_LOCATION)!!
        return LatLng(referencePoint.latitude +.1, referencePoint.longitude)
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val REFERENCE_POINT_LABEL = "reference.label.key"
        private const val REFERENCE_POINT_LOCATION = "reference.location.key"
        private const val SELECTED_POINT_LOCATION = "selected.location.key"

        @JvmStatic
        fun newInstance(
            /**
             * The name of the reference point shown in the map.
             */
            referencePointLabel: String,
            /**
             * The location of the reference point shown in the map.
             */
            referencePointLocation: LatLng,
            /**
             * The location of the user's previously selected location.
             * May be null if user has not selected anything yet.
             */
            selectedPointLocation: LatLng?
        ) = PlacePickerFragment().apply {
            arguments = bundleOf(
                REFERENCE_POINT_LABEL to referencePointLabel,
                REFERENCE_POINT_LOCATION to referencePointLocation,
                SELECTED_POINT_LOCATION to selectedPointLocation
            )
        }
    }
}
