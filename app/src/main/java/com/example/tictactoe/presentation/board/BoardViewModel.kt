package com.example.tictactoe.presentation.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.tictactoe.presentation.mappers.BoardMapper
import com.example.tictactoe.presentation.mappers.CellMapper
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    private val boardMapper: BoardMapper,
    private val cellMapper: CellMapper,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val isEnabledBoardInteraction: MutableStateFlow<Boolean> = MutableStateFlow(true)

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
    fun getViewState(): StateFlow<ViewStates> = viewState.viewState

    /**
     * It lets the [BoardViewModel] consumers subscribe to get board interaction state updates.
     */
    fun getBoardInteractionState(): StateFlow<Boolean> = isEnabledBoardInteraction

    /**
     * It lets the [BoardViewModel] consumers subscribe to get board updates via [StateFlow].
     */
    fun getBoardState(): StateFlow<BoardUiData> = viewState.boardState
        .map { boardMapper.mapToPresentation(it) }
        .stateIn(
            viewModelScope,
            initialValue = BoardUiData(emptyList()),
            started = SharingStarted.WhileSubscribed(2000)
        )

    fun onRestartButtonClicked() {
        viewState.showLoading()
        viewModelScope.launch {
            clearBoardUseCase().getOrNull()?.let {
                viewState.updateTurnToXPlayer()
                isEnabledBoardInteraction.value = true
            }
        }
    }

    fun onCellClicked(cell: CellUiData) {
        viewModelScope.launch {
            val currentPlayer = getNextPlayerUseCase()
            selectCell(cellMapper.mapToDomain(cell), currentPlayer)
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
                    is GameState.Winner -> when (currentPlayer) {
                        OPlayer -> viewState.displayOPlayerWinner()
                        XPlayer -> viewState.displayXPlayerWinner()
                    }
                }
                isEnabledBoardInteraction.value = state is GameState.Ongoing
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
}
