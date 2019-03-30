package mx.itesm.taxiunico.profile

import com.google.firebase.firestore.FirebaseFirestore
import mx.itesm.taxiunico.models.UserProfile


class UserService {
    private val db = FirebaseFirestore.getInstance()

    fun getProfile(userId: String, onComplete: (UserProfile?) -> Unit) {
        db.collection(USER_COLLECTION_KEY).document(userId).get().addOnCompleteListener {
            val res = it.result.toObject(UserProfile::class.java)
            onComplete(res)
        }
    }

    fun updateProfile(userId: String, userProfile: UserProfile, onSuccess: () -> Unit) {
        db.collection(USER_COLLECTION_KEY).document(userId).set(userProfile).addOnSuccessListener {
            onSuccess()
        }
    }

    companion object {
        const val USER_COLLECTION_KEY = "users"
    }
}


