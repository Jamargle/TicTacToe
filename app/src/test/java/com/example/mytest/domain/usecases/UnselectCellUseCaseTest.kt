package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class UnselectCellUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val unSelectCellUseCase = UnselectCellUseCase(boardRepository)

    @Test
    fun `returns success if given cell is already unselected`() = runBlockingTest {
        assertTrue(unSelectCellUseCase(Cell(0, 0, Clear)).isSuccess)
    }

    @Test
    fun `returns failure if cell is selected but unSelection is not fine from boardRepository`() =
        runBlockingTest {
            val givenCell = Cell(0, 0, XSelected)
            coEvery { boardRepository.clearCellSelection(givenCell) } returns Result.failure(
                Throwable("")
            )
            assertTrue(unSelectCellUseCase(givenCell).isFailure)
        }

    @Test
    fun `returns success if cell is selected and unSelection is fine from boardRepository`() =
        runBlockingTest {
            val givenCell = Cell(0, 0, XSelected)
            every { boardRepository.clearCellSelection(givenCell) } returns
                    Result.success(Unit)

            assertTrue(unSelectCellUseCase(Cell(0, 0, XSelected)).isSuccess)
        }
}
