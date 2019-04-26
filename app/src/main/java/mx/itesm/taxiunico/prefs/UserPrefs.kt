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

    fun clear() {
        prefs.edit().clear().apply()
    }

    var userUUID: String?
        get() = prefs.getString(USER_UUID_KEY, null)
        set(value) {
            prefs.edit().putString(USER_UUID_KEY, value).apply()
        }

    var userProfile: UserProfile
        get() = UserProfile(
                name = prefs.getString(USER_NAME_KEY, ""),
                country = prefs.getString(USER_COUNTRY_KEY, ""),
                email = prefs.getString(USER_EMAIL_KEY, ""),
                phone = prefs.getString(USER_PHONE_KEY, ""),
                userType = UserType.valueOf(prefs.getString(USER_TYPE_KEY, UserType.TRAVELER.toString()))
        )

        set(value) {
            prefs.edit().apply {
                putString(USER_NAME_KEY, value.name)
                putString(USER_COUNTRY_KEY, value.country)
                putString(USER_EMAIL_KEY, value.email)
                putString(USER_PHONE_KEY, value.phone)
                putString(USER_TYPE_KEY, value.userType.toString())

            }.apply()
        }

    companion object {
        private const val FILE_KEY = "user.prefs.file.key"
        private const val USER_UUID_KEY = "user.uuid.key"
        private const val USER_NAME_KEY = "user.name.key"
        private const val USER_COUNTRY_KEY = "user.country.key"
        private const val USER_EMAIL_KEY = "user.email.key"
        private const val USER_PHONE_KEY = "user.phone.key"
        private const val USER_TYPE_KEY = "user.type.key"

    }
}