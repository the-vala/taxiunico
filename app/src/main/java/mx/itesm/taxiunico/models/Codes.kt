package mx.itesm.taxiunico.models

 data class Codes (
    val destination: String = "",
    val fRegreso: String = "",
    val fSalida: String = "",
    @JvmField var isRound: Boolean = false,
    val origin: String = ""
 )

