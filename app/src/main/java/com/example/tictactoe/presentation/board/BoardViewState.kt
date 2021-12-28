package com.example.tictactoe.presentation.board

import com.example.tictactoe.domain.model.Board
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class BoardViewState
@Inject constructor() {

    internal val viewState: MutableStateFlow<ViewStates> = MutableStateFlow(ViewStates.Loading)
    internal val boardState: MutableStateFlow<Board> = MutableStateFlow(Board(emptyList()))

    fun showLoading() = viewState.update { ViewStates.Loading }

    fun updateBoard(board: Board) {
        boardState.value = board
    }

    fun updateTurnToXPlayer() = viewState.update { ViewStates.XPlaying }

    fun updateTurnToOPlayer() = viewState.update { ViewStates.OPlaying }

    fun displayDrawGame() = viewState.update { ViewStates.Finished.Draw }

    fun displayOPlayerWinner() = viewState.update { ViewStates.Finished.Win.OPlayerWins }
    fun displayXPlayerWinner() = viewState.update { ViewStates.Finished.Win.XPlayerWins }

    fun displayErrorMessage() = viewState.update { ViewStates.Finished.Error }
}
