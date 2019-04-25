package mx.itesm.taxiunico.models

data class DriverProfile(
    var name: String = "",
    var lastname: String = "",
    var country: String = "",
    var email: String = "",
    var phone: String = "",
    val userType: UserType = UserType.DRIVER
)