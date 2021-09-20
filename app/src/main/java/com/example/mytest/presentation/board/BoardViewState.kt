package com.example.mytest.presentation.board

import androidx.lifecycle.MutableLiveData
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Player

class BoardViewState {

    internal val viewState: MutableLiveData<ViewStates> = MutableLiveData()
    internal val boardState: MutableLiveData<Board> = MutableLiveData()
    internal val playerTurn: MutableLiveData<Player> = MutableLiveData()

    fun showLoading() = viewState.postValue(ViewStates.Loading)

    fun updateBoard(board: Board) {
        boardState.postValue(board)
    }

    fun updateTurn(player: Player) =
        playerTurn.postValue(player)
}
