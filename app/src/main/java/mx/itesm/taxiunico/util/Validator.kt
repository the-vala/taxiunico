package mx.itesm.taxiunico.util

abstract class Validator {
    companion object {
        fun  valReservationCode(code:String):Boolean {
            if (code.length  < 6) {
                return false //
            }
            return true
        }
    }
}
