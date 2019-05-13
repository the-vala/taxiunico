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
package mx.itesm.taxiunico.prefs

import android.content.Context
import mx.itesm.taxiunico.models.UserProfile
import mx.itesm.taxiunico.models.UserType

/**
 * recursos:
 * https://kotlinlang.org/docs/reference/properties.html getters, setters
 * https://medium.com/viithiisys/android-manage-user-session-using-shared-preferences-1187cb9c5cd8: abstraccion de prefs
 */
class UserPrefs(
    context: Context
) {
    private val prefs = context.applicationContext.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE)

    /**
     * Función que vacía las preferencias del usuario
     */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /**
     * Función para obtener userID
     */
    var userUUID: String?
        get() = prefs.getString(USER_UUID_KEY, null)
        set(value) {
            prefs.edit().putString(USER_UUID_KEY, value).apply()
        }

    /**
     * Función para obtener perfil de usuario
     */
    var userProfile: UserProfile
        get() = UserProfile(
            name = prefs.getString(USER_NAME_KEY, ""),
            country = prefs.getString(USER_COUNTRY_KEY, ""),
            email = prefs.getString(USER_EMAIL_KEY, ""),
            phone = prefs.getString(USER_PHONE_KEY, ""),
            cityHub = prefs.getString(USER_CITY_HUB_KEY, ""),
            surveyScore = prefs.getFloat(SCORE_KEY, 0.0F).toDouble(),
            userType = UserType.valueOf(prefs.getString(USER_TYPE_KEY, UserType.TRAVELER.toString()))
        )

        /**
         * Función para guardar información de usuario
         */
        set(value) {
            prefs.edit().apply {
                putString(USER_NAME_KEY, value.name)
                putString(USER_COUNTRY_KEY, value.country)
                putString(USER_EMAIL_KEY, value.email)
                putString(USER_PHONE_KEY, value.phone)
                putString(USER_TYPE_KEY, value.userType.toString())
                putString(USER_CITY_HUB_KEY, value.cityHub)
                putFloat(SCORE_KEY, value.surveyScore.toFloat())
            }.apply()
        }

    companion object {
        private const val FILE_KEY = "user.prefs.file.key"
        private const val USER_UUID_KEY = "user.uuid.key"
        private const val USER_NAME_KEY = "user.name.key"
        private const val USER_COUNTRY_KEY = "user.country.key"
        private const val USER_EMAIL_KEY = "user.email.key"
        private const val USER_PHONE_KEY = "user.phone.key"
        private const val USER_CITY_HUB_KEY = "user.city.key"
        private const val USER_TYPE_KEY = "user.type.key"
        private const val SCORE_KEY = "score.key"
    }
}