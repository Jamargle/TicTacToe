package com.example.tictactoe.app.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.example.tictactoe.presentation.mappers.BoardMapper as PresentationBoardMapper
import com.example.tictactoe.presentation.mappers.CellMapper as PresentationCellMapper

@Module
class MapperModule {

    @Singleton
    @Provides
    fun provideBoardMapper(): PresentationBoardMapper = PresentationBoardMapper

    @Singleton
    @Provides
    fun providePresentationCellMapper(): PresentationCellMapper = PresentationCellMapper

}
