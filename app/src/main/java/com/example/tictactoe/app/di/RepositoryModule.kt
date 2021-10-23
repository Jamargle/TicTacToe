package com.example.tictactoe.app.di

import com.example.tictactoe.data.OnMemoryBoardRepository
import com.example.tictactoe.domain.repositories.BoardRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Named(ON_MEMORY_BOARD_REPOSITORY)
    @Provides
    fun provideOnMemoryBoardRepository(repository: OnMemoryBoardRepository): BoardRepository =
        repository

    companion object {
        const val ON_MEMORY_BOARD_REPOSITORY = "Inject:OnMemoryBoardRepository"
    }
}
