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
package mx.itesm.taxiunico.models

/**
 * Clase para definir tipo de usuario
 */
enum class UserType {
    TRAVELER,
    DRIVER
}

/**
 * Modelo de Usuario
 */
data class UserProfile(
    var name: String = "",
    var country: String = "",
    var email: String = "",
    var phone: String = "",
    val userType: UserType = UserType.TRAVELER,
    val cityHub: String = "",
    val surveyScore: Double = 0.0,
    var tripCount: Int = 0
)