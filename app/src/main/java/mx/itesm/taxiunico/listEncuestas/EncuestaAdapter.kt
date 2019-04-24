package mx.itesm.taxiunico

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_encuesta.*

class EncuestaAdapter(
    private val context: Context,
    private val encuestas: MutableList<Encuesta>
): BaseAdapter() {

    var viewHolderCount:Int = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val encuestaHolder: EncuestaViewHolder
        val rowView: View
        val encuesta: Encuesta = getItem(position) as Encuesta

        if (convertView == null) {
            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            rowView = inflater.inflate(R.layout.row_encuesta, parent, false)
            encuestaHolder = EncuestaViewHolder(rowView)
            rowView.tag = encuestaHolder
            viewHolderCount++

        } else {

            rowView = convertView
            encuestaHolder = convertView.tag as EncuestaViewHolder
        }
        encuestaHolder.bind(encuesta)
        return rowView
    }

    override fun getItem(position: Int): Any {
        return encuestas.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return encuestas.size
    }

    /**
     * Clase interna que aloja los elementos de un renglón
     */
    inner class EncuestaViewHolder(override val containerView: View) : LayoutContainer {

        fun bind(encuesta: Encuesta) {
            //text_calificacion.text = "Calificación: " + encuesta.calificacion.toString()
            //text_fecha.text = "Fecha: " + encuesta.fecha
            //text_id_encuesta.text = "ID: " + encuesta.id.toString()
        }
    }
}
