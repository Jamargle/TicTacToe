package com.example.mytest.presentation.board

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mytest.MainCoroutineRule
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.usecases.CheckGameStateUseCase
import com.example.mytest.domain.usecases.GetBoardStateUseCase
import com.example.mytest.domain.usecases.GetNextPlayerUseCase
import com.example.mytest.domain.usecases.SelectCellUseCase
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
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

    private val viewState = mockk<BoardViewState>(relaxed = true)
    private val getBoardState = mockk<GetBoardStateUseCase>()
    private val checkGameState = mockk<CheckGameStateUseCase>()
    private val getNextPlayer = mockk<GetNextPlayerUseCase>()
    private val selectCell = mockk<SelectCellUseCase>()

    private fun createBoardViewModel() = BoardViewModel(
        viewState,
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
    fun `viewModel starts displaying loading and a clear board after`() = runBlockingTest {
        val board = Board(emptyList())
        coEvery { getBoardState() } returns flowOf(board)

        createBoardViewModel()

        verify(ordering = Ordering.ORDERED) {
            viewState.showLoading()
            viewState.updateBoard(board)
        }
    }

    @Test
    fun `viewModel displays next players turn`() = runBlockingTest {
        coEvery { getNextPlayer() } returns XPlayer
        createBoardViewModel()
        verify { viewState.updateTurn(XPlayer) }
    }

    @Test
    fun `onCellClicked marks cell as selected for the next player if selectCell returns success`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify(exactly = 1) { selectCell(givenCell, expectedPlayer) }
        }

    @Test
    fun `onCellClicked checks game state after marking cell as selected with success`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify(ordering = Ordering.ORDERED) {
                selectCell(givenCell, expectedPlayer)
                checkGameState()
            }
        }

    @Test
    fun `onCellClicked displays Draw state of the game when checkGameState returns Draw`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Draw)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify { viewState.displayDrawGame() }
        }

    @Test
    fun `onCellClicked displays Winner with player when checkGameState returns Winner`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Winner(expectedPlayer))

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify { viewState.displayWinner(expectedPlayer) }
        }

    @Test
    fun `onCellClicked updates turn when checkGameState returns Ongoing`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Ongoing)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify { viewState.updateTurn(OPlayer) }
            coVerify(exactly = 0) {
                viewState.displayDrawGame()
                viewState.displayWinner(any())
            }
        }

    @Test
    fun `onCellClicked does nothing if the given cell is already selected`() = runBlockingTest {
        val expectedPlayer = XPlayer
        coEvery { getNextPlayer() } returns expectedPlayer
        val givenCell = Cell(0, 0, OSelected)
        coEvery { selectCell(givenCell, expectedPlayer) } returns
                Result.failure(Throwable(""))

        val viewModel = createBoardViewModel()
        viewModel.onCellClicked(givenCell)

        verify(exactly = 0) { viewState.updateTurn(OPlayer) }
    }

}
