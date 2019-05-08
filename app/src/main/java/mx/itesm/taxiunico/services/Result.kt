package mx.itesm.taxiunico.services

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val result: T): Result<T>()
    data class Failure(val result: Throwable?): Result<Nothing>()
}