package com.example.mytest.domain.utils

import com.example.mytest.app.di.ApplicationModule.Companion.BACKGROUND_DISPATCHER
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class WinnerCheckHelper
@Inject constructor(
    @Named(BACKGROUND_DISPATCHER) private val backgroundDispatcher: CoroutineDispatcher
) {

    /**
     * Checks in a worker thread if the last selected cell is the 3rd in a
     * line for the same [Player].
     * It returns the [Player] that wins or null if none of the player wins.
     */
    suspend fun checkForWinner(board: Board): Player? =
        withContext(backgroundDispatcher) {
            board.cells.filterNot { it.state == Clear }.let { selectedCells ->
                with(selectedCells.last()) {
                    if (board.checkLineInNeighboursForCell(this)) {
                        return@withContext getPlayer()
                    }
                }
            }
            null
        }

    private fun Board.checkLineInNeighboursForCell(cell: Cell) =
        checkLineHorizontally(cell) || checkLineVertically(cell) || checkLineDiagonals(cell)

    private fun Board.checkLineHorizontally(cell: Cell): Boolean {
        val selectedCellsInRow = cells.filter { it.row == cell.row && it.state == cell.state }
        if (selectedCellsInRow.size > 2) {
            return true
        }
        return false
    }

    private fun Board.checkLineVertically(cell: Cell): Boolean {
        val selectedCellsInColumn =
            cells.filter { it.column == cell.column && it.state == cell.state }
        if (selectedCellsInColumn.size > 2) {
            return true
        }
        return false
    }

    private fun Board.checkLineDiagonals(cell: Cell): Boolean {
        val boardSideSize = sqrt(cells.size.toDouble()).toInt()
        val selectedCellInDiagonalRight = mutableListOf<Cell>()
        for (i in 0 until boardSideSize) {
            val cellInDiagonal = cells.find {
                it.row == i &&
                        it.column == i &&
                        it.state == cell.state
            }
            if (cellInDiagonal != null) {
                selectedCellInDiagonalRight.add(cell)
            }
        }
        if (selectedCellInDiagonalRight.size >= 3) {
            return true
        }

        val selectedCellInDiagonalLeft = mutableListOf<Cell>()

        var columnIndex = 2
        for (rowIndex in 0 until boardSideSize) {
            val cellInDiagonal = cells.find {
                it.row == rowIndex &&
                        it.column == columnIndex &&
                        it.state == cell.state
            }
            columnIndex--
            if (cellInDiagonal != null) {
                selectedCellInDiagonalLeft.add(cell)
            }
        }

        return selectedCellInDiagonalLeft.size >= 3
    }

    private fun Cell.getPlayer() =
        when (state) {
            OSelected -> OPlayer
            XSelected -> XPlayer
            else -> null
        }
}
