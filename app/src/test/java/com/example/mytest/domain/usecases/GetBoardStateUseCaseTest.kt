package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
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
        coEvery { boardRepository.getBoard() } returns flowOf(expectedBoard)
        getBoardUseCase().collect {
            assertEquals(expectedBoard, it)
        }
    }
}
