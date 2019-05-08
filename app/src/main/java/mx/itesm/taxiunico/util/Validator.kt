package mx.itesm.taxiunico.util

abstract class Validator {
    companion object {
        /**
         * Confirma que el reservation code tenga una logitud superior a 6
         * */
        fun  valReservationCode(code:String):Boolean {
            if (code.length  < 6) {
                return false
            }
            return true
        }
    }
}
