package com.cm.taxi.di

import com.cm.taxi.data.remote.TaxiApiService
import com.cm.taxi.data.repository.BaordRepositoryImpl
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.data.repository.UserRepository
import com.cm.taxi.data.repository.UserRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val repositoryModule = module {
    factory { provideTaxiService(retrofit = get()) }
    factory<UserRepository> { UserRepositoryImpl(taxiApiService = get(), errorAdapter = get()) }
    factory<BoardRepository> { BaordRepositoryImpl(taxiApiService = get(), errorAdapter = get()) }
}

internal fun provideTaxiService(retrofit: Retrofit): TaxiApiService = retrofit.create(TaxiApiService::class.java)