package mx.itesm.taxiunico

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import mx.itesm.taxiunico.auth.LoginActivity
import mx.itesm.taxiunico.billing.AddPaymentActivity
import mx.itesm.taxiunico.billing.PaymentFormsActivity
import mx.itesm.taxiunico.billing.PaymentMethod
import mx.itesm.taxiunico.profile.UserProfile
import mx.itesm.taxiunico.profile.UserProfileActivity
import mx.itesm.taxiunico.profile.UserService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        startActivity(Intent(this, LoginActivity::class.java))

        openProfile.setOnClickListener {
            startActivity(Intent(this, PaymentFormsActivity::class.java))
        }
    }
}
