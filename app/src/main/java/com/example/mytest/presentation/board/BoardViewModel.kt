package com.example.mytest.presentation.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.usecases.CheckGameStateUseCase
import com.example.mytest.domain.usecases.GetBoardStateUseCase
import com.example.mytest.domain.usecases.GetNextPlayerUseCase
import com.example.mytest.domain.usecases.SelectCellUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BoardViewModel(
    private val getBoardState: GetBoardStateUseCase,
    private val checkGameState: CheckGameStateUseCase,
    private val getNextPlayer: GetNextPlayerUseCase,
    private val selectCell: SelectCellUseCase
) : ViewModel() {

    private val boardState: MutableLiveData<Board> = MutableLiveData()

    init {
        viewModelScope.launch {
            getBoardState.invoke().collect {
                boardState.postValue(it)
            }
        }
    }

    fun getBoardState(): LiveData<Board> = boardState

    fun onCellClicked(cell: Cell) {
        viewModelScope.launch {
            val nextPlayer = getNextPlayer()
            if (cell.state != Clear) {
                return@launch
            }
            val result = selectCell(cell, nextPlayer)
            result.fold(
                onFailure = {
                    println("Error selecting the cell $cell")
                },
                onSuccess = {
                    println("Cell $cell marked as selected by $nextPlayer")
                }
            )
        }
    }

}
