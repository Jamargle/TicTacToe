package com.example.mytest.presentation.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.usecases.CheckGameStateUseCase
import com.example.mytest.domain.usecases.GetBoardStateUseCase
import com.example.mytest.domain.usecases.GetNextPlayerUseCase
import com.example.mytest.domain.usecases.SelectCellUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class BoardViewModel(
    private val viewState: BoardViewState,
    private val getBoardStateUseCase: GetBoardStateUseCase,
    private val checkGameStateUseCase: CheckGameStateUseCase,
    private val getNextPlayerUseCase: GetNextPlayerUseCase,
    private val selectCellUseCase: SelectCellUseCase,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    init {
        viewState.showLoading()
        observeBoardUpdates()
        viewModelScope.launch {
            viewState.updateTurn(getNextPlayerUseCase())
        }
    }

    private fun observeBoardUpdates() {
        viewModelScope.launch {
            getBoardStateUseCase()
                .flowOn(backgroundDispatcher)
                .collect {
                    viewState.updateBoard(it)
                }
        }
    }

    /**
     * It lets the [BoardViewModel] consumers subscribe to get view state updates.
     */
    fun getViewState(): LiveData<ViewStates> = viewState.viewState

    /**
     * It lets the [BoardViewModel] consumers subscribe to get board updates.
     */
    fun getBoardState(): LiveData<Board> = viewState.boardState

    /**
     * It lets the [BoardViewModel] consumers subscribe to get player turn updates.
     */
    fun getPlayerTurnState(): LiveData<Player> = viewState.playerTurn

    fun onCellClicked(cell: Cell) {
        viewModelScope.launch {
            val currentPlayer = getNextPlayerUseCase()
            selectCell(cell, currentPlayer)
        }
    }

    private suspend fun selectCell(cell: Cell, currentPlayer: Player) {
        selectCellUseCase(cell, currentPlayer).fold(
            onFailure = {
                println("Error selecting the cell $cell")
            },
            onSuccess = { checkGameState(currentPlayer) }
        )
    }

    private suspend fun checkGameState(currentPlayer: Player) {
        checkGameStateUseCase().getOrNull()?.let { state ->
            when (state) {
                GameState.Draw -> viewState.displayDrawGame()
                GameState.Ongoing -> viewState.updateTurn(getNextPlayerUseCase())
                is GameState.Winner -> viewState.displayWinner(currentPlayer)
            }
        }
    }

}
