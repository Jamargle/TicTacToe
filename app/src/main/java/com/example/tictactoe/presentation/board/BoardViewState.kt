package com.example.tictactoe.presentation.board

import androidx.lifecycle.MutableLiveData
import com.example.tictactoe.domain.model.Board
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class BoardViewState
@Inject constructor() {

    internal val viewState: MutableLiveData<ViewStates> = MutableLiveData()
    internal val boardState: MutableStateFlow<Board> = MutableStateFlow(Board(emptyList()))

    fun showLoading() = viewState.postValue(ViewStates.Loading)

    fun updateBoard(board: Board) {
        boardState.value = board
    }

    fun updateTurnToXPlayer() = viewState.postValue(ViewStates.XPlaying)

    fun updateTurnToOPlayer() = viewState.postValue(ViewStates.OPlaying)

    fun displayDrawGame() = viewState.postValue(ViewStates.Finished.Draw)

    fun displayOPlayerWinner() = viewState.postValue(ViewStates.Finished.Win.OPlayerWins)
    fun displayXPlayerWinner() = viewState.postValue(ViewStates.Finished.Win.XPlayerWins)

    fun displayErrorMessage() = viewState.postValue(ViewStates.Finished.Error)
}
