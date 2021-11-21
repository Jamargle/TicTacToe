package com.example.tictactoe.presentation.board

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.tictactoe.MainCoroutineRule
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Clear
import com.example.tictactoe.domain.model.GameState
import com.example.tictactoe.domain.model.OPlayer
import com.example.tictactoe.domain.model.OSelected
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.domain.usecases.CheckGameStateUseCase
import com.example.tictactoe.domain.usecases.ClearBoardUseCase
import com.example.tictactoe.domain.usecases.GetBoardStateUseCase
import com.example.tictactoe.domain.usecases.GetNextPlayerUseCase
import com.example.tictactoe.domain.usecases.SelectCellUseCase
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    private val viewState = mockk<BoardViewState>(relaxed = true)
    private val getBoardState = mockk<GetBoardStateUseCase>()
    private val checkGameState = mockk<CheckGameStateUseCase>()
    private val getNextPlayer = mockk<GetNextPlayerUseCase>()
    private val selectCell = mockk<SelectCellUseCase>()
    private val clearBoardUseCase = mockk<ClearBoardUseCase>()

    private fun createBoardViewModel() = BoardViewModel(
        viewState,
        getBoardState,
        checkGameState,
        getNextPlayer,
        selectCell,
        clearBoardUseCase
    )

    @Before
    fun setup() {
        coEvery { getBoardState.invoke() } returns flowOf(Board(emptyList()))
    }
    // endregion

    @Test
    fun `on viewModel start, the interaction with the board is enabled`() = runBlockingTest {
        val board = Board(emptyList())
        coEvery { getBoardState() } returns flowOf(board)

        val viewModel = createBoardViewModel()

        assertEquals(true, viewModel.getBoardInteractionState().value)
    }

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
        verify { viewState.updateTurnToXPlayer() }
    }

    @Test
    fun `viewModel exposes state of the view through getViewState`() = runBlockingTest {
        val expectedViewState = mockk<MutableLiveData<ViewStates>>()
        every { viewState.viewState } returns expectedViewState
        val viewModel = createBoardViewModel()

        assertEquals(
            expectedViewState,
            viewModel.getViewState()
        )
    }

    @Test
    fun `viewModel exposes state of the board through getBoardState`() = runBlockingTest {
        val expectedBoardState = mockk<MutableLiveData<Board>>()
        every { viewState.boardState } returns expectedBoardState
        val viewModel = createBoardViewModel()

        assertEquals(
            expectedBoardState,
            viewModel.getBoardState()
        )
    }

    @Test
    fun `viewModel exposes current player turn through getBoardState`() = runBlockingTest {
        val expectedPlayerTurn = mockk<MutableLiveData<Player>>()
        every { viewState.playerTurn } returns expectedPlayerTurn
        val viewModel = createBoardViewModel()

        assertEquals(
            expectedPlayerTurn,
            viewModel.getPlayerTurnState()
        )
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
    fun `onCellClicked displays error message if selecting cell returns failure`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, any()) } returns Result.failure(Throwable(""))

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            verify { viewState.displayErrorMessage() }
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
    fun `onCellClicked disables board interaction when checkGameState returns Draw`() =
        runBlockingTest {
            val nextPlayer = OPlayer
            coEvery { getNextPlayer() } returns nextPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, nextPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Draw)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            assertEquals(false, viewModel.getBoardInteractionState().value)
        }

    @Test
    fun `onCellClicked displays XPlayer Winner when checkGameState returns Winner for XPlayer`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Winner(expectedPlayer))

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify { viewState.displayXPlayerWinner() }
        }

    @Test
    fun `onCellClicked displays OPlayer Winner when checkGameState returns Winner for OPlayer`() =
        runBlockingTest {
            val expectedPlayer = OPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Winner(expectedPlayer))

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify { viewState.displayOPlayerWinner() }
        }

    @Test
    fun `onCellClicked disables board interaction when checkGameState returns Winner`() =
        runBlockingTest {
            val nextPlayer = XPlayer
            coEvery { getNextPlayer() } returns nextPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, nextPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Winner(nextPlayer))

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            assertEquals(false, viewModel.getBoardInteractionState().value)
        }

    @Test
    fun `onCellClicked updates turn to XPlayer when checkGameState returns Ongoing and current player is OPlayer`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Ongoing)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            // first time when viewmodel is created and second when cell selected
            coVerify(atLeast = 2) { viewState.updateTurnToXPlayer() }
            coVerify(exactly = 0) {
                viewState.displayDrawGame()
                viewState.displayXPlayerWinner()
                viewState.displayOPlayerWinner()
            }
        }

    @Test
    fun `onCellClicked updates turn to OPlayer when checkGameState returns Ongoing and current player is XPlayer`() =
        runBlockingTest {
            val nextPlayer = OPlayer
            coEvery { getNextPlayer() } returns nextPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, nextPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Ongoing)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify { viewState.updateTurnToOPlayer() }
            coVerify(exactly = 0) {
                viewState.displayDrawGame()
                viewState.displayXPlayerWinner()
                viewState.displayOPlayerWinner()
            }
        }

    @Test
    fun `onCellClicked enables board interaction when checkGameState returns Ongoing`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = Cell(0, 0, Clear)
            coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Ongoing)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            assertEquals(true, viewModel.getBoardInteractionState().value)
        }

    @Test
    fun `onCellClicked displays error if checking the game returns failure`() = runBlockingTest {
        val expectedPlayer = XPlayer
        coEvery { getNextPlayer() } returns expectedPlayer
        val givenCell = Cell(0, 0, OSelected)
        coEvery { selectCell(givenCell, expectedPlayer) } returns Result.success(Unit)
        coEvery { checkGameState() } returns Result.failure(Throwable(""))

        val viewModel = createBoardViewModel()
        viewModel.onCellClicked(givenCell)

        // updateTurnToXPlayer is called when viewModel is created
        verify(exactly = 1) { viewState.updateTurnToXPlayer() }
        verify(exactly = 0) { viewState.updateTurnToOPlayer() }
        verify { viewState.displayErrorMessage() }
    }

    @Test
    fun `onRestartButtonClicked clears the board and sets the turn to XPlayer`() = runBlockingTest {
        coEvery { clearBoardUseCase() } returns Result.success(Unit)

        val viewModel = createBoardViewModel()
        viewModel.onRestartButtonClicked()

        coVerify { clearBoardUseCase() }
        verify { viewState.updateTurnToXPlayer() }
    }

    @Test
    fun `onRestartButtonClicked displays loading while resetting the board`() = runBlockingTest {
        coEvery { clearBoardUseCase() } returns Result.success(Unit)

        val viewModel = createBoardViewModel()
        viewModel.onRestartButtonClicked()

        verify { viewState.showLoading() }
    }

    @Test
    fun `onRestartButtonClicked enables interaction with the board`() = runBlockingTest {
        coEvery { clearBoardUseCase() } returns Result.success(Unit)

        val viewModel = createBoardViewModel()
        viewModel.onRestartButtonClicked()

        assertEquals(true, viewModel.getBoardInteractionState().value)
    }

    @Test
    fun `onGeneralErrorPositiveButtonClicked restarts the game`() = runBlockingTest {
        coEvery { clearBoardUseCase() } returns Result.success(Unit)

        val viewModel = createBoardViewModel()
        viewModel.onGeneralErrorPositiveButtonClicked()

        coVerify { clearBoardUseCase() }
        verify {
            viewState.showLoading()
            viewState.updateTurnToXPlayer()
        }
    }

}
