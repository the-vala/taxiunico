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
package mx.itesm.taxiunico.survey

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_encuesta.*
import mx.itesm.taxiunico.R

class SurveyAdapter(
    private val context: Context,
    private var surveys: MutableList<Survey>
): BaseAdapter() {

    var viewHolderCount:Int = 0
    //lateinit var adapter: SurveyAdapter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val surveyHolder: SurveyViewHolder
        val rowView: View
        val survey: Survey = getItem(position) as Survey

        if (convertView == null) {
            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            rowView = inflater.inflate(R.layout.row_encuesta, parent, false)
            surveyHolder = SurveyViewHolder(rowView)
            rowView.tag = surveyHolder
            viewHolderCount++

        } else {
            rowView = convertView
            surveyHolder = convertView.tag as SurveyViewHolder
        }
        surveyHolder.bind(survey)
        return rowView
    }

    override fun getItem(position: Int): Any {
        return surveys.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return surveys.size
    }

    fun updateAdapter(mutableListBooks: MutableList<Survey>) {
        surveys = mutableListBooks
        notifyDataSetChanged()
    }

    inner class SurveyViewHolder(override val containerView: View) : LayoutContainer {
        fun bind(survey: Survey) {
            text_calificacion.text = context.getString(R.string.calificacion, survey.score)
            text_fecha.text = context.getString(R.string.fecha_encuesta, survey.date)
        }
    }
}
