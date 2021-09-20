package com.example.mytest.presentation.board

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mytest.MainCoroutineRule
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.usecases.CheckGameStateUseCase
import com.example.mytest.domain.usecases.GetBoardStateUseCase
import com.example.mytest.domain.usecases.GetNextPlayerUseCase
import com.example.mytest.domain.usecases.SelectCellUseCase
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BoardViewModelTest {

    // region test setup
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val getBoardState = mockk<GetBoardStateUseCase>()
    private val checkGameState = mockk<CheckGameStateUseCase>()
    private val getNextPlayer = mockk<GetNextPlayerUseCase>()
    private val selectCell = mockk<SelectCellUseCase>()

    private fun createBoardViewModel() = BoardViewModel(
        getBoardState,
        checkGameState,
        getNextPlayer,
        selectCell
    )

    @Before
    fun setup() {
        coEvery { getBoardState.invoke() } returns flowOf(Board(emptyList()))
    }
    // endregion

    @Test
    fun `viewModel starts with a clear board`() = runBlockingTest {
        val viewModel = createBoardViewModel()

        val selectedCells = viewModel.getBoardState().value?.cells?.filter { it.state != Clear }
        assertEquals(true, selectedCells?.isEmpty())
    }

    @Test
    fun `onCellClicked marks cell as selected for the next player if cell is clear`() {
        val expectedPlayer = XPlayer
        coEvery { getNextPlayer() } returns expectedPlayer
        val givenCell = Cell(0, 0, Clear)

        val viewModel = createBoardViewModel()
        viewModel.onCellClicked(givenCell)

        coVerify { selectCell(givenCell, expectedPlayer) }
    }

    @Test
    fun `onCellClicked does nothing if the given cell is already selected`() {
        val expectedPlayer = XPlayer
        coEvery { getNextPlayer() } returns expectedPlayer
        val givenCell = Cell(0, 0, OSelected)

        val viewModel = createBoardViewModel()
        viewModel.onCellClicked(givenCell)

        coVerify { selectCell(givenCell, expectedPlayer) wasNot Called }
    }

}
