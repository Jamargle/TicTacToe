package com.example.mytest.data

import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class OnMemoryBoardRepositoryTest {

    private val repository = OnMemoryBoardRepository(boardSideSize = 3)

    @Test
    fun `starts with clear board`() = runBlockingTest {
        val board = repository.getBoard().first()
        assertEquals(9, board.cells.filter { it.state == Clear }.size)

        assertNull(board.cells.find { it.state == XSelected })
        assertNull(board.cells.find { it.state == OSelected })
    }

    @Test
    fun `updateCellSelection returns failure when trying to update a cell that was already selected`() =
        runBlockingTest {
            val resultMove1 = repository.updateCellSelection(Cell(0, 0), XPlayer)
            val resultMove2 = repository.updateCellSelection(Cell(0, 0), XPlayer)
            val resultMove3 = repository.updateCellSelection(Cell(0, 0), OPlayer)

            assertTrue(resultMove1.isSuccess)
            assertTrue(resultMove2.isFailure)
            assertTrue(resultMove3.isFailure)
        }

    @Test
    fun `updateCellSelection updates board flow when board changes`() = runBlockingTest {
        val initialBoard = repository.getBoard().first()
        repository.updateCellSelection(Cell(0, 0), XPlayer)

        val boardAfterUpdate = repository.getBoard().first()

        with(initialBoard) {
            assertEquals(9, cells.filter { it.state == Clear }.size)
            assertEquals(0, cells.filter { it.state == XSelected }.size)
        }
        with(boardAfterUpdate) {
            assertEquals(8, cells.filter { it.state == Clear }.size)
            assertEquals(1, cells.filter { it.state == XSelected }.size)
        }
    }

    @Test
    fun `updateCellSelection returns success if selection changed`() = runBlockingTest {
        val result = repository.updateCellSelection(Cell(0, 0), XPlayer)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `clearCellSelection returns failure if given cell does not exist in the board`() =
        runBlockingTest {
            val resultXSelected = repository.clearCellSelection(Cell(10, 10, XSelected))
            assertTrue(resultXSelected.isFailure)
        }

    @Test
    fun `clearCellSelection returns success if selection was changed to Clear`() =
        runBlockingTest {
            repository.updateCellSelection(Cell(0, 1), XPlayer)
            repository.updateCellSelection(Cell(0, 2), OPlayer)

            val resultXSelected = repository.clearCellSelection(Cell(0, 1, XSelected))
            val resultOSelected = repository.clearCellSelection(Cell(0, 2, OSelected))

            assertTrue(resultXSelected.isSuccess)
            assertTrue(resultOSelected.isSuccess)
        }

    @Test
    fun `clearCellSelection returns success if selection was already Clear`() =
        runBlockingTest {
            val resultClear = repository.clearCellSelection(Cell(0, 0, Clear))

            assertTrue(resultClear.isSuccess)
        }

}
