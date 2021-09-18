package com.example.mytest.domain.utils

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WinnerCheckHelperTest {

    // region test setup
    private val testDispatcher = TestCoroutineDispatcher()

    private val winnerCheckHelper = WinnerCheckHelper(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
    // endregion

    @Test
    fun `checkForWinner returns null if the board is not completed`() = runBlockingTest {
        assertNull(winnerCheckHelper.checkForWinner(Board(cleanBoardCells)))
    }

    @Test
    fun `checkForWinner returns null if the board is completed but does not contain a line for any player`() =
        runBlockingTest {
            val board = Board(
                cells = listOf(
                    Cell(0, 0, OSelected),
                    Cell(0, 1, XSelected),
                    Cell(0, 2, OSelected),
                    Cell(1, 0, XSelected),
                    Cell(1, 1, XSelected),
                    Cell(1, 2, OSelected),
                    Cell(2, 0, XSelected),
                    Cell(2, 1, OSelected),
                    Cell(2, 2, XSelected)
                )
            )

            assertNull(winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in first row with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(0)
                add(0, Cell(0, 0, XSelected))
                removeAt(1)
                add(1, Cell(0, 1, XSelected))
                removeAt(1)
                add(2, Cell(0, 2, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in second row with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(3)
                add(3, Cell(1, 0, XSelected))
                removeAt(4)
                add(4, Cell(1, 1, XSelected))
                removeAt(5)
                add(5, Cell(1, 2, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in third row with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(6)
                add(6, Cell(2, 0, XSelected))
                removeAt(7)
                add(7, Cell(2, 1, XSelected))
                removeAt(8)
                add(8, Cell(2, 2, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in first column with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(0)
                add(0, Cell(0, 0, XSelected))
                removeAt(3)
                add(3, Cell(1, 0, XSelected))
                removeAt(6)
                add(6, Cell(2, 0, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in second column with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(1)
                add(1, Cell(0, 1, XSelected))
                removeAt(4)
                add(4, Cell(1, 1, XSelected))
                removeAt(7)
                add(7, Cell(2, 1, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in third column with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(2)
                add(2, Cell(0, 2, XSelected))
                removeAt(5)
                add(5, Cell(1, 2, XSelected))
                removeAt(8)
                add(8, Cell(2, 2, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in one diagonal with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(0)
                add(0, Cell(0, 0, XSelected))
                removeAt(4)
                add(4, Cell(1, 1, XSelected))
                removeAt(8)
                add(8, Cell(2, 2, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns XPlayer if the board contains all cells in the other diagonal with XSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(2)
                add(2, Cell(0, 2, XSelected))
                removeAt(4)
                add(4, Cell(1, 1, XSelected))
                removeAt(6)
                add(6, Cell(2, 0, XSelected))
            })

            assertEquals(XPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in first row with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(0)
                add(0, Cell(0, 0, OSelected))
                removeAt(1)
                add(1, Cell(0, 1, OSelected))
                removeAt(1)
                add(2, Cell(0, 2, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in second row with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(3)
                add(3, Cell(1, 0, OSelected))
                removeAt(4)
                add(4, Cell(1, 1, OSelected))
                removeAt(5)
                add(5, Cell(1, 2, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in third row with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(6)
                add(6, Cell(2, 0, OSelected))
                removeAt(7)
                add(7, Cell(2, 1, OSelected))
                removeAt(8)
                add(8, Cell(2, 2, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in first column with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(0)
                add(0, Cell(0, 0, OSelected))
                removeAt(3)
                add(3, Cell(1, 0, OSelected))
                removeAt(6)
                add(6, Cell(2, 0, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in second column with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(1)
                add(1, Cell(0, 1, OSelected))
                removeAt(4)
                add(4, Cell(1, 1, OSelected))
                removeAt(7)
                add(7, Cell(2, 1, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in third column with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(2)
                add(2, Cell(0, 2, OSelected))
                removeAt(5)
                add(5, Cell(1, 2, OSelected))
                removeAt(8)
                add(8, Cell(2, 2, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in one diagonal with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(0)
                add(0, Cell(0, 0, OSelected))
                removeAt(4)
                add(4, Cell(1, 1, OSelected))
                removeAt(8)
                add(8, Cell(2, 2, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    @Test
    fun `checkForWinner returns OPlayer if the board contains all cells in the other diagonal with OSelected`() =
        runBlockingTest {
            val board = Board(cells = cleanBoardCells.toMutableList().apply {
                removeAt(2)
                add(2, Cell(0, 2, OSelected))
                removeAt(4)
                add(4, Cell(1, 1, OSelected))
                removeAt(6)
                add(6, Cell(2, 0, OSelected))
            })

            assertEquals(OPlayer, winnerCheckHelper.checkForWinner(board))
        }

    private val cleanBoardCells = listOf(
        Cell(0, 0),
        Cell(0, 1),
        Cell(0, 2),
        Cell(1, 0),
        Cell(1, 1),
        Cell(1, 2),
        Cell(2, 0),
        Cell(2, 1),
        Cell(2, 2)
    )
}
