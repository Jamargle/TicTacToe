package com.example.tictactoe.app.ui

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.example.tictactoe.app.ui.theme.TicTacToeTheme
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.utils.getCellUiData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BoardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun boardDisplaysCellsAndHasClickHandling() {
        val boardSize = 3
        val isInteractionEnabled = true
        val boardData = BoardUiData(getCellUiData(boardSize))
        val cellClickWatcher = mutableSetOf<CellUiData>()
        composeTestRule.setContent {
            TicTacToeTheme {
                Board(
                    boardSize = boardSize,
                    boardData = boardData,
                    isInteractionEnabled = isInteractionEnabled,
                    onCellClicked = { cellUiData -> cellClickWatcher.add(cellUiData) }
                )
            }
        }

        for (i in boardData.cells.indices) {
            composeTestRule.onRoot().onChildAt(i)
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
        }
        assertEquals(boardSize * boardSize, cellClickWatcher.size)
    }

    @Test
    fun boardDisablesClickHandlingWhenInteractionEnabledIsFalse() {
        val boardSize = 3
        val isInteractionEnabled = false
        val boardData = BoardUiData(getCellUiData(boardSize))
        val cellClickWatcher = mutableSetOf<CellUiData>()
        composeTestRule.setContent {
            TicTacToeTheme {
                Board(
                    boardSize = boardSize,
                    boardData = boardData,
                    isInteractionEnabled = isInteractionEnabled,
                    onCellClicked = { cellUiData -> cellClickWatcher.add(cellUiData) }
                )
            }
        }

        for (i in boardData.cells.indices) {
            composeTestRule.onRoot().onChildAt(i)
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
        }
        assertEquals(0, cellClickWatcher.size)
    }
}
