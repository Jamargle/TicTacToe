package com.example.mytest.presentation.board

import androidx.lifecycle.MutableLiveData
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer

class BoardViewState {

    internal val viewState: MutableLiveData<ViewStates> = MutableLiveData()
    internal val boardState: MutableLiveData<Board> = MutableLiveData()
    internal val playerTurn: MutableLiveData<Player> = MutableLiveData()

    fun showLoading() = viewState.postValue(ViewStates.Loading)

    fun updateBoard(board: Board) = boardState.postValue(board)

    fun updateTurn(player: Player) = playerTurn.postValue(player)

    fun displayDrawGame() = viewState.postValue(ViewStates.Finished)

    fun displayWinner(winner: XPlayer) = viewState.postValue(ViewStates.Finished)
}
