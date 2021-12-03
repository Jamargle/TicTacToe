package com.example.tictactoe.presentation.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BoardUiDataTest {

    @Test
    fun `getCell in board by row and column returns cell if exists`() {
        val expectedCell = CellUiData(0, 0, XSelected)
        val board = BoardUiData(
            listOf(
                expectedCell,
                CellUiData(0, 1, OSelected),
                CellUiData(0, 2, XSelected),
                CellUiData(1, 0, OSelected),
                CellUiData(1, 1, XSelected),
                CellUiData(1, 2, OSelected),
                CellUiData(2, 0, XSelected),
                CellUiData(2, 1, OSelected),
                CellUiData(2, 2, XSelected)
            )
        )
        assertEquals(expectedCell, board.getCell(0, 0))
    }

    @Test
    fun `getCell in board by row and column returns null if it does not exist`() {
        val board = BoardUiData(
            listOf(
                CellUiData(0, 0, XSelected),
                CellUiData(0, 1, OSelected),
                CellUiData(0, 2, XSelected),
                CellUiData(1, 0, OSelected),
                CellUiData(1, 1, XSelected),
                CellUiData(1, 2, OSelected),
                CellUiData(2, 0, XSelected),
                CellUiData(2, 1, OSelected),
                CellUiData(2, 2, XSelected)
            )
        )
        assertNull(board.getCell(10, 0))
    }
}
