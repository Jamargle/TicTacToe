package com.example.tictactoe.domain.usecases

import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Clear
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.domain.repositories.BoardRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectCellUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val selectCellUseCase = SelectCellUseCase(boardRepository)

    @Test
    fun `returns failure if selection is failure from boardRepository`() = runBlockingTest {
        val givenCell = Cell(0, 0, Clear)
        every { boardRepository.updateCellSelection(givenCell, XPlayer) } returns
                Result.failure(Throwable(""))

        assertTrue(selectCellUseCase(givenCell, XPlayer).isFailure)
    }

    @Test
    fun `returns success if selection is fine from boardRepository`() =
        runBlockingTest {
            val givenCell = Cell(0, 0, Clear)
            every { boardRepository.updateCellSelection(givenCell, XPlayer) } returns
                    Result.success(Unit)

            assertTrue(selectCellUseCase(Cell(0, 0), XPlayer).isSuccess)
        }
}
