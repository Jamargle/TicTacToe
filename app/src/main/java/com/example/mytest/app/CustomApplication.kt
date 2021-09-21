package com.example.mytest.app

import android.app.Application
import com.example.mytest.app.di.ApplicationComponent
import com.example.mytest.app.di.DaggerApplicationComponent

class CustomApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory().create(applicationContext)
    }
}
