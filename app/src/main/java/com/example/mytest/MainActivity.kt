package com.example.mytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initApplicationComponent()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun initApplicationComponent() {
        (applicationContext as CustomApplication)
            .appComponent
            .inject(this)
    }
}
