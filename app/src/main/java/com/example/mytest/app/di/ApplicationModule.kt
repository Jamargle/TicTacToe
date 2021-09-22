package com.example.mytest.app.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    @Named(BACKGROUND_DISPATCHER)
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @Named(BOARD_SIZE)
    fun provideBoardSize(): Int = 3

    companion object {
        const val BACKGROUND_DISPATCHER = "Inject:BackgroundDispatcher"
        const val BOARD_SIZE = "Inject:BoardSideSize"
    }
}
