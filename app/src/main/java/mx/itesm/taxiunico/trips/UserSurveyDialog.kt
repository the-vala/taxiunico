package mx.itesm.taxiunico.trips

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.itesm.taxiunico.R
import mx.itesm.taxiunico.models.Viaje
import mx.itesm.taxiunico.services.TripService
import mx.itesm.taxiunico.util.cost


class UserSurveyDialog(
    private val context: Context
) {
    fun show(tripId: String, viaje: Viaje) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_trip_survey, null)
        val builder = AlertDialog.Builder(context).setView(dialogView)
        val dialog = builder.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogView.findViewById<TextView>(R.id.surveyTotal).apply {
            text = context.getString(R.string.cost, viaje.cost())
        }

        dialogView.findViewById<TextView>(R.id.surveyDateTime).apply {
            text = viaje.completionDateTime.toDate().toString()
        }

        dialogView.findViewById<TextView>(R.id.name).apply {
            text = viaje.driverName
        }

        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar).apply {
            rating = INITIAL_RATING
        }

        // TODO(terminar de poner info de este layout)
        dialogView.findViewById<Button>(R.id.surveyConfirm).setOnClickListener {
            dialog.dismiss()

            TripService().addUserSurveyAnswer(
                tripId = tripId,
                rating = ratingBar.rating
            )
        }
    }

    companion object {
        private const val INITIAL_RATING = 3F
    }
}