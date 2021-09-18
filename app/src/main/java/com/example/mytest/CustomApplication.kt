package com.example.mytest

import android.app.Application
import com.example.mytest.di.ApplicationComponent
import com.example.mytest.di.DaggerApplicationComponent

class CustomApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory().create(applicationContext)
    }
}
