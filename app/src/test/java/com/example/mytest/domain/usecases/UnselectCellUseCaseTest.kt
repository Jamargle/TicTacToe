package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.repositories.BoardRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class UnselectCellUseCaseTest {

    private val boardRepository = mockk<BoardRepository>()
    private val unSelectCellUseCase = UnselectCellUseCase(boardRepository)

    @Test
    fun `returns failure if unSelection is not fine from boardRepository`() {
        val givenCell = Cell(0, 0, XSelected)
        every { boardRepository.clearCellSelection(givenCell) } returns
                Result.failure(Throwable(""))

        assertTrue(unSelectCellUseCase(givenCell).isFailure)
    }

    @Test
    fun `returns success if unSelection is fine from boardRepository`() {
        val givenCell = Cell(0, 0, XSelected)
        every { boardRepository.clearCellSelection(givenCell) } returns
                Result.success(Unit)

        assertTrue(unSelectCellUseCase(Cell(0, 0, XSelected)).isSuccess)
    }
}
