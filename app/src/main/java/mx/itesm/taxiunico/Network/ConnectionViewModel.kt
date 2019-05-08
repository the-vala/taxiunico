package mx.itesm.taxiunico.Network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import mx.itesm.taxiunico.util.Event

class ConnectionViewModel : ViewModel() {
    private val connectionState = MutableLiveData<Boolean>()

    fun setConnectionState(state: Boolean) {
        connectionState.value = state
    }
    fun getConnectionState(): LiveData<Boolean> = connectionState
}
