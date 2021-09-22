package com.example.mytest.app.di

import android.content.Context
import com.example.mytest.app.features.board.BoardFragment
import com.example.mytest.app.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        RepositoryModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(boardFragment: BoardFragment)

}
