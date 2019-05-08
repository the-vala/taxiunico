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
 * Modelo de Codigo de reservacion con informacion obtenida al momento de hacer la compra
 */
 data class Codes (
    val destination: String = "",
    val fRegreso: String = "",
    val fSalida: String = "",
    @JvmField var isRound: Boolean = false,
    val origin: String = ""
 )

