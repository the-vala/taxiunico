package mx.itesm.taxiunico.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import mx.itesm.taxiunico.util.Event


class TripConfigurationViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Event<LatLng>>()

    fun setSelectedLocation(location: LatLng) {
        locationLiveData.value = Event(location)
    }

    fun getLocation(): LiveData<Event<LatLng>> = locationLiveData
}
