package com.example.tictactoe.presentation.model.mappers

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.presentation.model.CellUiData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BoardMapperKtTest {

    @Test
    fun `Board_toBoardData returns BoardUiData with converted cell data`() {
        val givenCell = mockk<Cell>()
        val givenBoard = Board(listOf(givenCell))
        val expectedCellUiData = mockk<CellUiData>()
        val mockedMapper = mockk<(Cell) -> CellUiData>()
        every { mockedMapper(givenCell) } returns expectedCellUiData

        val result = givenBoard.toBoardData(mockedMapper)

        with(result) {
            assertEquals(1, cells.size)
            assertEquals(expectedCellUiData, cells[0])
        }
    }
}
