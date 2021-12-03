package com.example.tictactoe.app

import android.app.Application
import com.example.tictactoe.app.di.ApplicationComponent
import com.example.tictactoe.app.di.DaggerApplicationComponent

class CustomApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory().create(applicationContext)
    }
}
