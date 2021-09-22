package com.example.mytest.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytest.app.di.ApplicationModule.Companion.BACKGROUND_DISPATCHER
import com.example.mytest.domain.usecases.CheckGameStateUseCase
import com.example.mytest.domain.usecases.ClearBoardUseCase
import com.example.mytest.domain.usecases.GetBoardStateUseCase
import com.example.mytest.domain.usecases.GetNextPlayerUseCase
import com.example.mytest.domain.usecases.SelectCellUseCase
import com.example.mytest.presentation.board.BoardViewModel
import com.example.mytest.presentation.board.BoardViewState
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class BoardViewModelFactory
@Inject constructor(
    private val boardViewState: BoardViewState,
    private val getBoardStateUseCase: GetBoardStateUseCase,
    private val checkGameStateUseCase: CheckGameStateUseCase,
    private val getNextPlayerUseCase: GetNextPlayerUseCase,
    private val selectCellUseCase: SelectCellUseCase,
    private val clearBoardUseCase: ClearBoardUseCase,
    @Named(BACKGROUND_DISPATCHER) private val backgroundDispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        BoardViewModel(
            boardViewState,
            getBoardStateUseCase,
            checkGameStateUseCase,
            getNextPlayerUseCase,
            selectCellUseCase,
            clearBoardUseCase,
            backgroundDispatcher
        ) as? T
            ?: throw IllegalArgumentException("This factory can only create BoardViewModel instances")
}
