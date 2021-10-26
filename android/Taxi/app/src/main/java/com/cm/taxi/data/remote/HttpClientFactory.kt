package com.cm.taxi.data.remote

import okhttp3.OkHttpClient

class HttpClientFactory {
    private val httpClient by lazy {

        OkHttpClient()
    }

    fun create(): OkHttpClient.Builder {
        return httpClient.newBuilder()
    }
}