package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.repositories.BoardRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ClearBoardUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val unSelectCellUseCase = mockk<UnselectCellUseCase>()
    private val clearBoardUseCase = ClearBoardUseCase(boardRepository, unSelectCellUseCase)

    @Before
    fun setup() {
        val expectedBoard = Board(listOf(Cell(0, 0)))
        coEvery { boardRepository.getBoard() } returns MutableStateFlow(expectedBoard)
    }

    @Test
    fun `returns success if all the cells were unselected`() = runBlockingTest {
        every { unSelectCellUseCase(any()) } returns Result.success(Unit)
        assertTrue(clearBoardUseCase().isSuccess)
    }

    @Test
    fun `returns failure if unSelectCellUseCase returns failure for some cell`() = runBlockingTest {
        every { unSelectCellUseCase(any()) } returns Result.failure(Throwable(""))
        assertTrue(clearBoardUseCase().isFailure)
    }
}
