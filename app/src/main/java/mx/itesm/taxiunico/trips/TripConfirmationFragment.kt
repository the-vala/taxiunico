package mx.itesm.taxiunico.trips


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import mx.itesm.taxiunico.R

class TripConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trip_confirmation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.encuesta, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.show()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val confirm = dialogView.findViewById<Button>(R.id.califica)
        confirm.setOnClickListener {
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
            dialog.dismiss()
            Toast.makeText(requireContext(), "Rating: ${ratingBar.rating}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TripConfirmationFragment()
    }
}
