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
package mx.itesm.taxiunico.services

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.models.UserProfile
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.prefs.UserPrefs

class AuthService constructor(context: Context) {
    private val userService = UserService()
    private val auth = FirebaseAuth.getInstance()
    private val prefs = UserPrefs(context)

    companion object {
        private val TAG = AuthService::class.java.simpleName
    }

    fun getUserType(): UserType = prefs.userProfile.userType
    fun isUserAuthenticated(): Boolean = !prefs.userUUID.isNullOrBlank()
    fun getUserProfile(): UserProfile = prefs.userProfile
    fun getUserUid(): String? = prefs.userUUID

    /**
     * Returns true if user was able to authenticate
     */
    suspend fun authenticate(email: String, password: String): Result<Unit> =
        try {
            // Authenticate with firebase
            val result = coroutineScope {
                auth.signInWithEmailAndPassword(email, password).await()
            }

            prefs.userUUID = result.user.uid

            // Then, load user profile and save it to user prefs.
            // TODO(VER SI DEJAR ESTO O SACARLO PARA QUE TRUENE SI NO PUEDE TRAER EL PERFIL)
            coroutineScope {
                launch { userService.getProfile(result.user.uid)?.let { prefs.userProfile = it } }
            }

            Result.Success(Unit)
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
            Result.Failure(e)
        }
}

