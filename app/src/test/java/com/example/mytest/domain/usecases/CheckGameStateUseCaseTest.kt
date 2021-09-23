package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.repositories.BoardRepository
import com.example.mytest.domain.utils.WinnerCheckHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckGameStateUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val winnerCheckHelper = mockk<WinnerCheckHelper>()
    private val checkGameStateUseCase = CheckGameStateUseCase(
        boardRepository,
        winnerCheckHelper
    )

    @Test
    fun `returns Ongoing if there are clear cells`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0)))
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
        coEvery { winnerCheckHelper.checkForWinner(expectedBoard) } returns null

        assertEquals(GameState.Ongoing, checkGameStateUseCase().getOrNull())
    }

    @Test
    fun `returns Draw if there are not clear cells and no winner`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, XSelected)))
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
        coEvery { winnerCheckHelper.checkForWinner(expectedBoard) } returns null

        assertEquals(GameState.Draw, checkGameStateUseCase().getOrNull())
    }

    @Test
    fun `returns XPlayer as winner if there are not clear cells and XPlayer wins`() =
        runBlockingTest {
            val expectedBoard = Board(listOf(Cell(0, 0, XSelected)))
            coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
            coEvery { winnerCheckHelper.checkForWinner(expectedBoard) } returns XPlayer

            val state = checkGameStateUseCase().getOrNull()

            with(state as GameState.Winner) {
                assertEquals(XPlayer, winner)
            }
        }

    @Test
    fun `returns OPlayer as winner if there are not clear cells and OPlayer wins`() =
        runBlockingTest {
            val expectedBoard = Board(listOf(Cell(0, 0, XSelected)))
            coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
            coEvery { winnerCheckHelper.checkForWinner(expectedBoard) } returns OPlayer

            val state = checkGameStateUseCase().getOrNull()

            with(state as GameState.Winner) {
                assertEquals(OPlayer, winner)
            }
        }
}
