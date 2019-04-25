package mx.itesm.taxiunico.models

data class Viaje(
    var codeId: String,
    var dateTime: String,
    var origin: String,
    var destination: String,
    var driverName: String,
    var vehicle: String,
    var distance: Float,
    var cost: Float,
    var payment: String,
    var completed: Boolean,
    var imageUrl: String
)