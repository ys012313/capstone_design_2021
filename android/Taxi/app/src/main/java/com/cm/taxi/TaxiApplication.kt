package com.cm.taxi

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.cm.taxi.di.networkModule
import com.cm.taxi.di.repositoryModule
import com.cm.taxi.di.viewModelModule
import com.cm.taxi.util.Prefer
import com.google.android.libraries.places.api.Places
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaxiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Places.initialize(applicationContext, "AIzaSyDATAotaZR_IK9O99nQ9xq9IIdBUqPJSsc")
        Places.createClient(this)
        Prefer.init(this)
        L.initialize("young",true)
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@TaxiApplication)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}