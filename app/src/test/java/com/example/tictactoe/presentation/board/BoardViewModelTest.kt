package com.example.tictactoe.presentation.board

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tictactoe.MainCoroutineRule
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.GameState
import com.example.tictactoe.domain.model.OPlayer
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.domain.usecases.CheckGameStateUseCase
import com.example.tictactoe.domain.usecases.ClearBoardUseCase
import com.example.tictactoe.domain.usecases.GetBoardStateUseCase
import com.example.tictactoe.domain.usecases.GetNextPlayerUseCase
import com.example.tictactoe.domain.usecases.SelectCellUseCase
import com.example.tictactoe.presentation.mappers.BoardMapper
import com.example.tictactoe.presentation.mappers.CellMapper
import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.presentation.model.Clear
import com.example.tictactoe.presentation.model.OSelected
import io.mockk.Ordering
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.tictactoe.domain.model.Clear as DomainClear

@ExperimentalCoroutinesApi
class BoardViewModelTest {

    // region test setup
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val viewState = mockk<BoardViewState>(relaxed = true)
    private val getBoardState = mockk<GetBoardStateUseCase>()
    private val checkGameState = mockk<CheckGameStateUseCase>(relaxed = true)
    private val getNextPlayer = mockk<GetNextPlayerUseCase>(relaxed = true)
    private val selectCell = mockk<SelectCellUseCase>()
    private val boardMapper = mockk<BoardMapper>()
    private val cellMapper = mockk<CellMapper>()
    private val clearBoardUseCase = mockk<ClearBoardUseCase>()

    private fun createBoardViewModel() = BoardViewModel(
        viewState,
        getBoardState,
        checkGameState,
        getNextPlayer,
        selectCell,
        clearBoardUseCase,
        boardMapper,
        cellMapper
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
        val expectedViewState = mockk<MutableStateFlow<ViewStates>>()
        every { viewState.viewState } returns expectedViewState
        val viewModel = createBoardViewModel()

        assertEquals(
            expectedViewState,
            viewModel.getViewState()
        )
    }

    @Test
    fun `onCellClicked marks cell as selected for the next player if selectCell returns success`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify(exactly = 1) { selectCell(domainCell, expectedPlayer) }
        }

    @Test
    fun `onCellClicked checks game state after marking cell as selected with success`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            coVerify(ordering = Ordering.ORDERED) {
                selectCell(domainCell, expectedPlayer)
                checkGameState()
            }
        }

    @Test
    fun `onCellClicked displays error message if selecting cell returns failure`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, any()) } returns Result.failure(Throwable(""))

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            verify { viewState.displayErrorMessage() }
        }

    @Test
    fun `onCellClicked displays Draw state of the game when checkGameState returns Draw`() =
        runBlockingTest {
            val expectedPlayer = XPlayer
            coEvery { getNextPlayer() } returns expectedPlayer
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, nextPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, nextPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, nextPlayer) } returns Result.success(Unit)
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
            val givenCell = CellUiData(0, 0, Clear)
            val domainCell = Cell(0, 0, DomainClear)
            every { cellMapper.mapToDomain(givenCell) } returns domainCell
            coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)
            coEvery { checkGameState() } returns Result.success(GameState.Ongoing)

            val viewModel = createBoardViewModel()
            viewModel.onCellClicked(givenCell)

            assertEquals(true, viewModel.getBoardInteractionState().value)
        }

    @Test
    fun `onCellClicked displays error if checking the game returns failure`() = runBlockingTest {
        val expectedPlayer = XPlayer
        coEvery { getNextPlayer() } returns expectedPlayer
        val givenCell = CellUiData(0, 0, OSelected)
        val domainCell = Cell(0, 0, DomainClear)
        every { cellMapper.mapToDomain(givenCell) } returns domainCell
        coEvery { selectCell(domainCell, expectedPlayer) } returns Result.success(Unit)
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
}
