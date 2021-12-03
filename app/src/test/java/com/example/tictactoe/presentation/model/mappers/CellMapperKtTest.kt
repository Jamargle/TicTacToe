package com.example.tictactoe.presentation.model.mappers

import com.example.tictactoe.presentation.model.CellUiData
import org.junit.Assert.assertEquals
import org.junit.Test
import com.example.tictactoe.domain.model.Cell as DomainCell
import com.example.tictactoe.domain.model.Clear as DomainClear
import com.example.tictactoe.domain.model.OSelected as DomainOSelected
import com.example.tictactoe.domain.model.XSelected as DomainXSelected
import com.example.tictactoe.presentation.model.Clear as PresentationClear
import com.example.tictactoe.presentation.model.OSelected as PresentationOSelected
import com.example.tictactoe.presentation.model.XSelected as PresentationXSelected

class CellMapperKtTest {

    @Test
    fun `DomainCell_toCellUiData returns converted CellUiData for DomainCell Clear`() {
        val givenCell = DomainCell(1, 2, DomainClear)

        with(givenCell.toCellUiData()) {
            assertEquals(1, column)
            assertEquals(2, row)
            assertEquals(PresentationClear, state)
        }
    }

    @Test
    fun `DomainCell_toCellUiData returns converted CellUiData for DomainCell OSelected`() {
        val givenCell = DomainCell(1, 2, DomainOSelected)

        with(givenCell.toCellUiData()) {
            assertEquals(1, column)
            assertEquals(2, row)
            assertEquals(PresentationOSelected, state)
        }
    }

    @Test
    fun `DomainCell_toCellUiData returns converted CellUiData for DomainCell XSelected`() {
        val givenCell = DomainCell(1, 2, DomainXSelected)

        with(givenCell.toCellUiData()) {
            assertEquals(1, column)
            assertEquals(2, row)
            assertEquals(PresentationXSelected, state)
        }
    }

    @Test
    fun `CellUiData_toCell returns converted Cell for CellUiData Clear`() {
        val givenCell = CellUiData(1, 2, PresentationClear)

        with(givenCell.toCell()) {
            assertEquals(1, column)
            assertEquals(2, row)
            assertEquals(DomainClear, state)
        }
    }

    @Test
    fun `CellUiData_toCell returns converted Cell for CellUiData OSelected`() {
        val givenCell = CellUiData(1, 2, PresentationOSelected)

        with(givenCell.toCell()) {
            assertEquals(1, column)
            assertEquals(2, row)
            assertEquals(DomainOSelected, state)
        }
    }

    @Test
    fun `CellUiData_toCell returns converted Cell for CellUiData XSelected`() {
        val givenCell = CellUiData(1, 2, PresentationXSelected)

        with(givenCell.toCell()) {
            assertEquals(1, column)
            assertEquals(2, row)
            assertEquals(DomainXSelected, state)
        }
    }
}
