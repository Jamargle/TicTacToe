package com.example.tictactoe.presentation.board

import androidx.lifecycle.MutableLiveData
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Player
import javax.inject.Inject

class BoardViewState
@Inject constructor() {

    internal val viewState: MutableLiveData<ViewStates> = MutableLiveData()
    internal val boardState: MutableLiveData<Board> = MutableLiveData(Board(emptyList())) // TODO refactor to StateFlow for this to not be null

    @Deprecated("It will be removed when view state and game state get unified")
    internal val playerTurn: MutableLiveData<Player> = MutableLiveData()

    fun showLoading() = viewState.postValue(ViewStates.Loading)

    fun updateBoard(board: Board) {
        boardState.value = board
    }

    fun updateTurnToXPlayer() = viewState.postValue(ViewStates.XPlaying)

    fun updateTurnToOPlayer() = viewState.postValue(ViewStates.OPlaying)

    fun displayDrawGame() = viewState.postValue(ViewStates.Finished.Draw)

    fun displayWinner(winner: Player) = viewState.postValue(ViewStates.Finished.Win(winner))

    fun displayErrorMessage() = viewState.postValue(ViewStates.Finished.Error)
}
