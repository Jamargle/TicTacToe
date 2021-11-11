package com.example.tictactoe.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tictactoe.R
import com.example.tictactoe.app.di.ApplicationModule.Companion.BOARD_SIZE
import com.example.tictactoe.app.di.BoardViewModelFactory
import com.example.tictactoe.app.ui.BoardScreen
import com.example.tictactoe.app.ui.theme.TicTacToeTheme
import com.example.tictactoe.domain.model.OPlayer
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.presentation.board.BoardViewModel
import com.example.tictactoe.presentation.board.ViewStates
import com.example.tictactoe.presentation.model.mappers.toCell
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
            TicTacToeTheme { // TODO fix dark theme

                val boardState by boardViewModel.getBoardStateFlow().collectAsState()
                val gameStateLabelState by gameStateLabel.collectAsState()

                BoardScreen(
                    boardSize = boardSize,
                    gameStateLabel = gameStateLabelState,
                    boardData = boardState,
                    onCellClicked = { boardViewModel.onCellClicked(it.toCell()) },
                    onRestartButtonClicked = boardViewModel::onRestartButtonClicked
                )
            }
        }
    }

    private fun initGameStateObserver() {
        // TODO move this logic to ViewModel if possible
        boardViewModel.getViewState().observe(this) {
            val stringRes = when (it) {
                ViewStates.Finished.Draw -> R.string.game_finish_with_draw
                ViewStates.Finished.Error -> R.string.general_error_message
                is ViewStates.Finished.Win -> when (it.winner) {
                    OPlayer -> R.string.game_finish_with_winner_o_player
                    XPlayer -> R.string.game_finish_with_winner_x_player
                }
                ViewStates.OPlaying -> R.string.next_player_title_label_O
                ViewStates.XPlaying -> R.string.next_player_title_label_X
                ViewStates.Loading -> R.string.game_state_loading
            }
            gameStateLabel.value = getString(stringRes)
        }
    }

    private fun initApplicationComponent() {
        (applicationContext as CustomApplication)
            .appComponent
            .inject(this)
    }
}
