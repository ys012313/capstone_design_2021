package com.cm.taxi.util

import com.cm.taxi.data.remote.ResponseError
import com.cm.taxi.data.remote.error.Failure
import com.cm.taxi.data.remote.error.NetworkConnectionLostSuddenly
import com.cm.taxi.data.remote.error.SSLError
import com.cm.taxi.data.remote.error.ServiceBodyFailure
import com.cm.taxi.data.remote.error.TimeOut
import com.squareup.moshi.JsonAdapter
import okio.BufferedSource
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

fun Throwable.parseException(
    adapter: JsonAdapter<ResponseError>,
): Failure {
    return when (this) {
        is SocketTimeoutException -> TimeOut
        is SSLException -> NetworkConnectionLostSuddenly
        is SSLHandshakeException -> SSLError
        is HttpException -> {
            val errorService = adapter.parseError(response()?.errorBody()?.source())
            if (errorService != null) {
                ServiceBodyFailure(
                    internalCode = errorService.status,
                    internalMessage = errorService.message
                )
            } else {
                Failure.UnexpectedFailure(
                    message = "Service ERROR BODY does not match."
                )
            }
        }
        else -> Failure.UnexpectedFailure(
            message = message ?: "Exception not handled caused an Unknown failure"
        )
    }
}

private fun JsonAdapter<ResponseError>.parseError(
    json: BufferedSource?,
): ResponseError? {
    return if (json != null) {
        fromJson(json)
    } else {
        null
    }
}