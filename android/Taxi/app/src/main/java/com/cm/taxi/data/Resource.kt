package com.cm.taxi.data

import android.system.ErrnoException
import com.cm.taxi.L
import com.cm.taxi.data.remote.ResponseError
import com.cm.taxi.data.remote.error.Failure
import com.cm.taxi.util.parseException
import com.squareup.moshi.JsonAdapter
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: Failure) : Resource<T>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: Failure) = Error<T>(message)
    }
}

//suspend fun <T> getResult(call: suspend () -> Response<T>, errorAdapter: JsonAdapter<ResponseError>): Resource<T> {
//    try {
//        val response = call()
//        if (response.isSuccessful) {
//            val body = response.body()
//            if (body != null) return Resource.success(body)
//        }
//        return Resource.error(Failure.UnexpectedFailure("${response.code()} ${response.message()}"))
//    } catch (e: Exception) {
//        return Resource.error(e.parseException(errorAdapter))
//    }
//}

//서버랑 주고 받는 쪽.
inline fun <reified T> Response<T>.getResult(errorAdapter: JsonAdapter<ResponseError>): Resource<T> {
    try {
        L.i(":::::::::::::여긴오나?")
        if (isSuccessful) {
            val responseBody = body()
            if (responseBody != null) return Resource.success(responseBody)
        }
        L.i(":::::::::::::여긴오나?")
        return Resource.error(Failure.UnexpectedFailure("${code()} ${message()}"))
    } catch (e: HttpException) {
        L.e("::::e " + e)
        return Resource.error(e.parseException(errorAdapter))
    } catch (e: ConnectException) {
        L.e("::::e " + e)
        return Resource.error(e.parseException(errorAdapter))
    } catch (e: ErrnoException) {
        L.e("::::e " + e)
        return Resource.error(e.parseException(errorAdapter))
    } catch (e: Exception) {
        L.e("::::e " + e)
        return Resource.error(e.parseException(errorAdapter))
    }
}

