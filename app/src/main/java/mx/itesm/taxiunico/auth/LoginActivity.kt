package mx.itesm.taxiunico.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import mx.itesm.taxiunico.R
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import mx.itesm.taxiunico.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            signIn(loginInputEmail.text.toString(), loginInputPass.text.toString())
        }

        loginRegistrateBtn.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(this,"Successful: ${user?.email}",
                        Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.putExtra(USER, user?.email)
                    startActivity(mainIntent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = loginInputEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginInputEmail.error = "Required."
            valid = false
        } else {
            loginInputEmail.error = null
        }

        val password = loginInputPass.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginInputPass.error = "Required."
            valid = false
        } else {
            loginInputPass.error = null
        }

        return valid
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val USER = "currentUser"
    }
}
