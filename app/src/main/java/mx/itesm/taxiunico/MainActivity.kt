package mx.itesm.taxiunico

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = intent.extras ?: return
        val user = extras.getString(USER)

        mainTextView.text = user
    }

    companion object {
        private const val USER = "currentUser"
    }
}
