package com.example.tictactoe.domain.usecases

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class GetBoardStateUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val getBoardUseCase = GetBoardStateUseCase(boardRepository)

    @Test
    fun `returns flow with updates on board state`() = runBlockingTest {
        val expectedBoard = mockk<Board>()
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
        val board = getBoardUseCase().first()
        assertEquals(expectedBoard, board)
    }
}
