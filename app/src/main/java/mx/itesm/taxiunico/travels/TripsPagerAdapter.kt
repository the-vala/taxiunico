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
package mx.itesm.taxiunico.travels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class TripsPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager){

    private val fragmentList : MutableList<Fragment> = ArrayList()
    private val titleList : MutableList<String> = ArrayList()

    /**
     * regresa el elemento in indice position de fragmentList
     * */
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    /**
     * regresa el tamaño de fragmentList
     * */
    override fun getCount(): Int {
        return fragmentList.size
    }

    /**
     * Añade el fragmento a la lista y el title a la lista de strings
     * */
    fun addFragment(fragment: Fragment, title:String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    /**
     * Obtiene el string de titulo de position de la lista de titulos
     * */
    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}
