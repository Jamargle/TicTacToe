package com.example.tictactoe.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.R

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
