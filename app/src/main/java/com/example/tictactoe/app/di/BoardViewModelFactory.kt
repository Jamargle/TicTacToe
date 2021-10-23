package com.example.tictactoe.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoe.app.di.ApplicationModule.Companion.BACKGROUND_DISPATCHER
import com.example.tictactoe.domain.usecases.CheckGameStateUseCase
import com.example.tictactoe.domain.usecases.ClearBoardUseCase
import com.example.tictactoe.domain.usecases.GetBoardStateUseCase
import com.example.tictactoe.domain.usecases.GetNextPlayerUseCase
import com.example.tictactoe.domain.usecases.SelectCellUseCase
import com.example.tictactoe.presentation.board.BoardViewModel
import com.example.tictactoe.presentation.board.BoardViewState
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
