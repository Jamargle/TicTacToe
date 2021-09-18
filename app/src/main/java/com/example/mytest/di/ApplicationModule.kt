package com.example.mytest.di

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
    @Named("BackgroundDispatcher")
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

}
