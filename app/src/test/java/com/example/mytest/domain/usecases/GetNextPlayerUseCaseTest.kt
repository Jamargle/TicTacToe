package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class GetNextPlayerUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val getNextPlayerUseCase = GetNextPlayerUseCase(boardRepository)

    @Test
    fun `returns XPlayer to start the game`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, Clear), Cell(0, 1, Clear)))
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
        assertEquals(XPlayer, getNextPlayerUseCase())
    }

    @Test
    fun `returns XPlayer if previous move was from OPlayer`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, Clear), Cell(0, 1, OSelected)))
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
        assertEquals(XPlayer, getNextPlayerUseCase())
    }

    @Test
    fun `returns OPlayer if previous move was from XPlayer`() = runBlockingTest {
        val expectedBoard = Board(listOf(Cell(0, 0, Clear), Cell(0, 1, XSelected)))
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
        assertEquals(OPlayer, getNextPlayerUseCase())
    }
}
