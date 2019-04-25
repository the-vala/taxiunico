package mx.itesm.taxiunico.models

 data class Codes (
    val destination: String = "",
    val fRegreso: String = "",
    val fSalida: String = "",
    var isRound: Boolean = true,
    val origin: String = ""
    )

