package mx.itesm.taxiunico.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import mx.itesm.taxiunico.MainActivity
import mx.itesm.taxiunico.models.UserProfile
import mx.itesm.taxiunico.models.UserType
import mx.itesm.taxiunico.prefs.UserPrefs
import mx.itesm.taxiunico.profile.UserService
import kotlin.coroutines.CoroutineContext

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
                async { auth.signInWithEmailAndPassword(email, password).await() }.await()
            }
            prefs.userUUID = result.user.uid

            // Then, load user profile and save it to user prefs.
            coroutineScope {
                launch { userService.getProfile(result.user.uid)?.let { prefs.userProfile = it } }
            }

            Result.Success(Unit)
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
            Result.Failure(e)
        }
    }


sealed class Result<out T: Any> {
    data class Success<out T: Any>(val result: T): Result<T>()
    data class Failure(val result: Throwable?): Result<Nothing>()
}

