package com.example.tictactoe.presentation.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.GameState
import com.example.tictactoe.domain.model.OPlayer
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.domain.usecases.CheckGameStateUseCase
import com.example.tictactoe.domain.usecases.ClearBoardUseCase
import com.example.tictactoe.domain.usecases.GetBoardStateUseCase
import com.example.tictactoe.domain.usecases.GetNextPlayerUseCase
import com.example.tictactoe.domain.usecases.SelectCellUseCase
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.mappers.toBoardData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BoardViewModel(
    private val viewState: BoardViewState,
    private val getBoardStateUseCase: GetBoardStateUseCase,
    private val checkGameStateUseCase: CheckGameStateUseCase,
    private val getNextPlayerUseCase: GetNextPlayerUseCase,
    private val selectCellUseCase: SelectCellUseCase,
    private val clearBoardUseCase: ClearBoardUseCase,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    init {
        viewState.showLoading()
        observeBoardUpdates()
        viewModelScope.launch {
            updateTurnForPlayer(getNextPlayerUseCase())
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
     * It lets the [BoardViewModel] consumers subscribe to get board updates via [StateFlow].
     */
    fun getBoardStateFlow(): StateFlow<BoardUiData> = viewState.boardState.asFlow()
        .map { it.toBoardData() }
        .stateIn(
            viewModelScope,
            initialValue = BoardUiData(emptyList()),
            started = SharingStarted.WhileSubscribed(2000)
        )

    /**
     * It lets the [BoardViewModel] consumers subscribe to get player turn updates.
     */
    @Deprecated("It will be removed after unifying with game state and view state")
    fun getPlayerTurnState(): LiveData<Player> = viewState.playerTurn

    fun onRestartButtonClicked() {
        viewState.showLoading()
        viewModelScope.launch {
            clearBoardUseCase().getOrNull()?.let { viewState.updateTurnToXPlayer() }
        }
    }

    fun onGeneralErrorPositiveButtonClicked() {
        onRestartButtonClicked()
    }

    fun onCellClicked(cell: Cell) {
        viewModelScope.launch {
            val currentPlayer = getNextPlayerUseCase()
            selectCell(cell, currentPlayer)
        }
    }

    private suspend fun selectCell(cell: Cell, currentPlayer: Player) {
        selectCellUseCase(cell, currentPlayer).fold(
            onSuccess = { checkGameState(currentPlayer) },
            onFailure = { viewState.displayErrorMessage() }
        )
    }

    private suspend fun checkGameState(currentPlayer: Player) {
        checkGameStateUseCase().fold(
            onSuccess = { state ->
                when (state) {
                    GameState.Draw -> viewState.displayDrawGame()
                    GameState.Ongoing -> updateTurnForPlayer(getNextPlayerUseCase())
                    is GameState.Winner -> viewState.displayWinner(currentPlayer)
                }
            },
            onFailure = { viewState.displayErrorMessage() }
        )
    }

    private fun updateTurnForPlayer(player: Player) {
        when (player) {
            OPlayer -> viewState.updateTurnToOPlayer()
            XPlayer -> viewState.updateTurnToXPlayer()
        }
    }

    // TODO remove this when LiveData is removed
    private fun <T> LiveData<T>.asFlow(): Flow<T> = callbackFlow {
        val observer = Observer<T> { value -> trySend(value).isSuccess }
        observeForever(observer)
        awaitClose { removeObserver(observer) }
    }.flowOn(Dispatchers.Main.immediate)
}
