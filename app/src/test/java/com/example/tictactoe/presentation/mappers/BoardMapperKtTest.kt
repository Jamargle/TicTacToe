package com.example.tictactoe.presentation.mappers

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.presentation.model.CellUiData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BoardMapperKtTest {

    @Test
    fun `mapToPresentation returns BoardUiData with converted cell data`() {
        val givenCell = mockk<Cell>()
        val givenBoard = Board(listOf(givenCell))
        val expectedCellUiData = mockk<CellUiData>()
        val mockedMapper = mockk<(Cell) -> CellUiData>()
        every { mockedMapper(givenCell) } returns expectedCellUiData

        val result = BoardMapper.mapToPresentation(givenBoard, mockedMapper)

        with(result) {
            assertEquals(1, cells.size)
            assertEquals(expectedCellUiData, cells[0])
        }
    }
}
