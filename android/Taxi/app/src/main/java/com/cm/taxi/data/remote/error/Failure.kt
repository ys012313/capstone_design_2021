package com.cm.taxi.data.remote.error

sealed class Failure {
    open class CustomFailure() : Failure()

    data class UnexpectedFailure(
        val message: String?,
    ) : Failure()
}
