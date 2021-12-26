package com.example.tictactoe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tictactoe.app.di.ApplicationModule.Companion.BOARD_SIZE
import com.example.tictactoe.app.di.BoardViewModelFactory
import com.example.tictactoe.app.ui.BoardScreen
import com.example.tictactoe.app.ui.theme.TicTacToeTheme
import com.example.tictactoe.presentation.board.BoardViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Named

class MainActivity : ComponentActivity() {

    @JvmField
    @field:[Inject Named(BOARD_SIZE)]
    var boardSize: Int = 0

    @Inject
    lateinit var boardViewModelFactory: BoardViewModelFactory

    private val boardViewModel by viewModels<BoardViewModel> { boardViewModelFactory }

    private val gameStateLabel = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        initApplicationComponent()
        super.onCreate(savedInstanceState)
        initGameStateObserver()
        setContent {
            TicTacToeTheme {

                val boardState by boardViewModel.getBoardState().collectAsState()
                val boardInteractionState by boardViewModel.getBoardInteractionState().collectAsState()
                val gameStateLabelState by gameStateLabel.collectAsState()

                BoardScreen(
                    boardSize = boardSize,
                    gameStateLabel = gameStateLabelState,
                    boardData = boardState,
                    isInteractionEnabled = boardInteractionState,
                    onCellClicked = boardViewModel::onCellClicked,
                    onRestartButtonClicked = boardViewModel::onRestartButtonClicked
                )
            }
        }
    }

    private fun initGameStateObserver() {
        boardViewModel.getViewState().observe(this) {
            gameStateLabel.value = getString(it.gameStateLabelRes)
        }
    }

    private fun initApplicationComponent() {
        (applicationContext as CustomApplication)
            .appComponent
            .inject(this)
    }
}
