package mx.itesm.taxiunico.survey

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class SurveyService {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        const val SURVEY_COLLECTION_KEY = "survey"
        private val TAG = SurveyService::class.java.simpleName
    }

    fun getSurveys(userId: String, onComplete: (MutableList<Survey>) -> Unit) {
        db.collection(SURVEY_COLLECTION_KEY)
            .document(userId).collection(SURVEY_COLLECTION_KEY).get().addOnSuccessListener {
                val surveys = it.documents
                    .filter { it.exists() }
                    .map { it.toObject(Survey::class.java)!! }.toMutableList()
                onComplete(surveys)
            }
    }

    suspend fun addSurvey(driverId: String, survey: Survey): Result<Unit> =
        try {
            coroutineScope{
                async {
                    db.collection(SURVEY_COLLECTION_KEY)
                        .document(driverId).collection(SURVEY_COLLECTION_KEY).add(survey).await()
                }.await()
            }
            Result.Success(Unit)
        } catch(e: Throwable) {
            Log.e(TAG, e.toString())
            Result.Failure(e)
        }

}

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val result: T): Result<T>()
    data class Failure(val result: Throwable?): Result<Nothing>()
}