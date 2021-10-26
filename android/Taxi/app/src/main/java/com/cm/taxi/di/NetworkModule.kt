package com.cm.taxi.di

import com.cm.taxi.BuildConfig
import com.cm.taxi.L
import com.cm.taxi.data.remote.HttpClientFactory
import com.cm.taxi.data.remote.ResponseError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { HttpClientFactory() }
    single { provideOkHttpBuilder(httpClientFactory = get()) }
    single { provideClient(clientBuilder = get()) }
    single { provideRetrofitBuilder(okHttpClient = get()) }
    single { provideMoshi() }
    single { provideJsonErrorAdapter(moshi = get()) }
}

internal fun provideOkHttpBuilder(
    httpClientFactory: HttpClientFactory,
): OkHttpClient.Builder {
    return httpClientFactory.create()
}

internal fun provideClient(
    clientBuilder: OkHttpClient.Builder,
): OkHttpClient {
    clientBuilder
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val originalHttpUrl = chain.request().url
            val url = originalHttpUrl.newBuilder().build()
            request.url(url)
            return@addInterceptor chain.proceed(request.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
    }
    return clientBuilder.build()
}

fun provideRetrofitBuilder(
    okHttpClient: OkHttpClient,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://18.118.125.146:8080")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}

internal fun provideMoshi(): Moshi {
    return Moshi.Builder().build()
}

fun provideJsonErrorAdapter(moshi: Moshi): JsonAdapter<ResponseError> {
    return moshi.adapter(ResponseError::class.java)
}


