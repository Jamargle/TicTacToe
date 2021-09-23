package com.example.mytest.data

import com.example.mytest.app.di.ApplicationModule.Companion.BOARD_SIZE
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.repositories.BoardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class OnMemoryBoardRepository
@Inject constructor(
    @Named(BOARD_SIZE) private val boardSideSize: Int
) : BoardRepository {

    private var boardFlow: MutableStateFlow<Board> = MutableStateFlow(getClearBoard())

    override suspend fun getBoard(): StateFlow<Board> = boardFlow

    override fun updateCellSelection(cell: Cell, player: Player): Result<Unit> =
        if (!cell.isClearInBoard()) {
            Result.failure(IllegalArgumentException("Cell was already selected"))
        } else {
            applySelectedStateForPlayer(cell, player)
        }

    override fun clearCellSelection(cell: Cell): Result<Unit> =
        boardFlow.value.getCell(cell)?.let { cellToUnSelect ->
            if (cellToUnSelect.state == Clear) {
                Result.success(Unit)
            } else {
                clearSelectionForCell(cellToUnSelect)
            }
        } ?: Result.failure(Throwable("Something went wrong while unselecting cell $cell"))

    private fun getClearBoard(): Board {
        val clearCells = mutableListOf<Cell>()
        for (row in 0 until boardSideSize) {
            for (column in 0 until boardSideSize) {
                clearCells.add(Cell(column, row))
            }
        }
        return Board(clearCells)
    }

    private fun Cell.isClearInBoard(): Boolean =
        boardFlow.value.cells.contains(this.copy(state = Clear))

    /**
     * Removes the cell that changes state if it was not previously selected. Then added it
     * at the end of the list with the new state.
     * This helps to know the last movement.
     */
    private fun applySelectedStateForPlayer(cell: Cell, player: Player): Result<Unit> {
        with(boardFlow.value.cells.toMutableList()) {
            if (remove(cell)) {
                val selectedCell = cell.copy(
                    state = when (player) {
                        OPlayer -> OSelected
                        XPlayer -> XSelected
                    }
                )
                add(selectedCell)
                boardFlow.value = Board(this)
                return Result.success(Unit)
            }
        }
        return Result.failure(Throwable("The cell could not be selected"))
    }

    /**
     * Removes the cell that changes state. Then is added at the beginning of the list
     * of cells so that the last movement is still the last cell in the list.
     */
    private fun clearSelectionForCell(cell: Cell): Result<Unit> {
        with(boardFlow.value.cells.toMutableList()) {
            if (remove(cell)) {
                add(0, cell.copy(state = Clear))
                boardFlow.value = Board(this)
                return Result.success(Unit)
            }
        }
        return Result.failure(Throwable("The cell could not be unselected"))
    }
}
