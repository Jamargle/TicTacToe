package com.example.mytest.domain.model.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.model.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectCellUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val selectCellUseCase = SelectCellUseCase(boardRepository)

    @Test
    fun `returns failure if cell does not exist in the board`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, Clear)))
        coEvery { boardRepository.getBoard() } returns flowOf(expectedBoard)
        assertTrue(selectCellUseCase(Cell(1, 1), XPlayer).isFailure)
    }

    @Test
    fun `returns failure if given cell is already selected`() = runBlockingTest {
        assertTrue(selectCellUseCase(Cell(0, 0, XSelected), XPlayer).isFailure)
    }

    @Test
    fun `returns failure if cell is already selected in the board`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, XSelected)))
        coEvery { boardRepository.getBoard() } returns flowOf(expectedBoard)
        assertTrue(selectCellUseCase(Cell(0, 0), XPlayer).isFailure)
    }

    @Test
    fun `returns success if cell is still clear and selection is fine from boardRepository`() =
        runBlockingTest {
            val givenCell = Cell(0, 0, Clear)
            val expectedBoard = Board(listOf(givenCell))
            coEvery { boardRepository.getBoard() } returns flowOf(expectedBoard)
            every { boardRepository.updateCellSelection(givenCell, XPlayer) } returns
                    Result.success(Unit)

            assertTrue(selectCellUseCase(Cell(0, 0), XPlayer).isSuccess)
        }
}
