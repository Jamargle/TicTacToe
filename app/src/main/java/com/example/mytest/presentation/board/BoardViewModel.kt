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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BoardViewModel(
    private val viewState: BoardViewState,
    private val getBoardState: GetBoardStateUseCase,
    private val checkGameState: CheckGameStateUseCase,
    private val getNextPlayer: GetNextPlayerUseCase,
    private val selectCell: SelectCellUseCase
) : ViewModel() {

    init {
        viewState.showLoading()
        viewModelScope.launch {
            getBoardState.invoke().collect {
                viewState.updateBoard(it)
                viewState.updateTurn(getNextTurn())
            }
        }
    }

    fun getBoardState(): LiveData<Board> = viewState.boardState

    fun onCellClicked(cell: Cell) {
        viewModelScope.launch {
            val currentPlayer = getNextPlayer()

            selectCell(cell, currentPlayer).fold(
                onFailure = {
                    println("Error selecting the cell $cell")
                },
                onSuccess = {
                    checkGameState.invoke().getOrNull()?.let { state ->
                        when (state) {
                            GameState.Draw -> viewState.displayDrawGame()
                            GameState.Ongoing -> viewState.updateTurn(getNextTurn(currentPlayer))
                            is GameState.Winner -> viewState.displayWinner(currentPlayer)
                        }
                    }
                }
            )
        }
    }

    private suspend fun getNextTurn(currentPlayer: Player? = null) =
        when (currentPlayer) {
            is XPlayer -> OPlayer
            is OPlayer -> XPlayer
            else -> getNextPlayer()
        }

}
