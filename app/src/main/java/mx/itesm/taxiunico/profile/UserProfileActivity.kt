package mx.itesm.taxiunico.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import mx.itesm.taxiunico.R

class UserProfileActivity : AppCompatActivity() {

    private val userService = UserService()

    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        render()

        button.setOnClickListener { saveProfile() }
    }


    private fun render() {
        userService.getProfile(auth.uid!!) {
            it?.let {
                nameInput.setText(it.name)
                lastnameInput.setText(it.lastname)
                emailInput.setText(it.email)
                phoneInput.setText(it.phone)
            }
        }
    }

    private fun saveProfile() {
        Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show()
        userService.updateProfile(auth.uid!!, UserProfile(
            name = nameInput.text.toString(),
            lastname = lastnameInput.text.toString(),
            email = emailInput.text.toString(),
            phone = phoneInput.text.toString()
        )) {
            Toast.makeText(this, "Perfil Guardado", Toast.LENGTH_SHORT).show()
        }
    }
}
