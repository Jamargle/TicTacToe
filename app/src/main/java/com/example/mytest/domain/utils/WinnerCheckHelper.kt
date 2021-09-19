package com.example.mytest.domain.utils

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

class WinnerCheckHelper(
    private val backgroundDispatcher: CoroutineDispatcher
) {

    /**
     * Checks in a worker thread if the last selected cell is the 3rd in a
     * line for the same [Player].
     * It returns the [Player] that wins or null if none of the player wins.
     */
    suspend fun checkForWinner(board: Board): Player? =
        withContext(backgroundDispatcher) {
            board.cells.filterNot { it.state == Clear }.let { selectedCells ->
                val lastSelectedCell = selectedCells.last()
                selectedCells.checkLineInNeighboursForCell(lastSelectedCell)
            }
        }

    private fun List<Cell>.checkLineInNeighboursForCell(cell: Cell): Player? {
        if (checkLineHorizontally(cell)) {
            return cell.getPlayer()
        }
        if (checkLineVertically(cell)) {
            return cell.getPlayer()
        }
        if (checkLineDiagonals(cell)) {
            return cell.getPlayer()
        }
        return null
    }

    private fun List<Cell>.checkLineHorizontally(cell: Cell): Boolean {
        val selectedCellsInRow = filter { it.row == cell.row && it.state == cell.state }
        if (selectedCellsInRow.size > 2) {
            return true
        }
        return false
    }

    private fun List<Cell>.checkLineVertically(cell: Cell): Boolean {
        val selectedCellsInColumn = filter { it.column == cell.column && it.state == cell.state }
        if (selectedCellsInColumn.size > 2) {
            return true
        }
        return false
    }

    private fun List<Cell>.checkLineDiagonals(cell: Cell): Boolean {
        // TODO to be implemented
        return false
    }

    private fun Cell.getPlayer() =
        when (state) {
            OSelected -> OPlayer
            XSelected -> XPlayer
            else -> null
        }
}
