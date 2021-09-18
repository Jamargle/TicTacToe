package com.example.mytest.domain.model.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.model.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckGameStateUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val checkGameStateUseCase = CheckGameStateUseCase(boardRepository)

    @Test
    fun `returns Ongoing if there are clear cells`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0)))
        coEvery { boardRepository.getBoard() } returns flowOf(expectedBoard)
        assertEquals(GameState.Ongoing, checkGameStateUseCase().getOrNull())
    }

    @Test
    fun `returns Draw if there are not clear cells and no winner`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, XSelected)))
        coEvery { boardRepository.getBoard() } returns flowOf(expectedBoard)
        assertEquals(GameState.Draw, checkGameStateUseCase().getOrNull())
    }

    @Test
    fun `returns XPlayer as winner if there are not clear cells and XPlayer wins`() =
        runBlockingTest {
            val state = checkGameStateUseCase().getOrNull()
            with(state as GameState.Winner) {
                assertEquals(XPlayer, winner)
            }
        }
}
