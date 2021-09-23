package com.example.mytest.presentation.board

import androidx.lifecycle.MutableLiveData
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Player
import javax.inject.Inject

class BoardViewState
@Inject constructor() {

    internal val viewState: MutableLiveData<ViewStates> = MutableLiveData()
    internal val boardState: MutableLiveData<Board> = MutableLiveData()
    internal val playerTurn: MutableLiveData<Player> = MutableLiveData()

    fun showLoading() = viewState.postValue(ViewStates.Loading)

    fun updateBoard(board: Board) {
        viewState.postValue(ViewStates.Playing)
        boardState.postValue(board)
    }

    fun updateTurn(player: Player) {
        viewState.postValue(ViewStates.Playing)
        playerTurn.postValue(player)
    }

    fun displayDrawGame() = viewState.postValue(ViewStates.Finished.Draw)

    fun displayWinner(winner: Player) = viewState.postValue(ViewStates.Finished.Win(winner))

    fun displayErrorMessage() {
        // TODO("Not yet implemented")
    }
}
