package mx.itesm.taxiunico.profile

import com.google.firebase.firestore.FirebaseFirestore


data class UserProfile(
    var name: String = "",
    var lastname: String = "",
    var country: String = "",
    var email: String = "",
    var phone: String = ""
)

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


