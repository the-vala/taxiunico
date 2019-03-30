package mx.itesm.taxiunico.models

data class UserProfile(
    var name: String = "",
    var lastname: String = "",
    var country: String = "",
    var email: String = "",
    var phone: String = ""
)