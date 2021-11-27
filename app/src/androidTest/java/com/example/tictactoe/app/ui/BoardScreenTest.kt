package com.example.tictactoe.app.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tictactoe.R
import com.example.tictactoe.app.MainActivity
import com.example.tictactoe.app.ui.theme.TicTacToeTheme
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.utils.getCellUiData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BoardScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun boardScreenHasHeaderAndGameStateLabelWithTheBoard() {
        val boardSize = 3
        val headerLabel = composeTestRule.activity.getString(R.string.board_screen_title)
        val restartButtonLabel = composeTestRule.activity.getString(R.string.restart_game_button)
        val gameStateLabel = "Some game state"
        val isInteractionEnabled = true
        val boardData = BoardUiData(getCellUiData(boardSize))
        val cellClickWatcher = mutableSetOf<CellUiData>()
        val restartButtonClickWatcher = mutableListOf<Unit>()
        composeTestRule.setContent {
            TicTacToeTheme {
                BoardScreen(
                    boardSize = boardSize,
                    gameStateLabel = gameStateLabel,
                    boardData = boardData,
                    isInteractionEnabled = isInteractionEnabled,
                    onCellClicked = { cellUiData -> cellClickWatcher.add(cellUiData) },
                    onRestartButtonClicked = { restartButtonClickWatcher.add(Unit) }
                )
            }
        }

        composeTestRule.onNodeWithText(headerLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(gameStateLabel).assertIsDisplayed()
        // TODO test cell clicks when we know how to find the correct nodes
        composeTestRule.onNodeWithText(restartButtonLabel).assertIsDisplayed().performClick()
        assertEquals(1, restartButtonClickWatcher.size)
    }
}
