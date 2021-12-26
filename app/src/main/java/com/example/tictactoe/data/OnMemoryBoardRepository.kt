package com.example.tictactoe.data

import com.example.tictactoe.app.di.ApplicationModule.Companion.BOARD_SIZE
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.CellState
import com.example.tictactoe.domain.model.Clear
import com.example.tictactoe.domain.repositories.BoardRepository
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

    override fun updateCellSelection(cell: Cell, selection: CellState): Result<Unit> =
        updateSelection(cell, selection)

    override fun clearCellSelection(cell: Cell): Result<Unit> =
        updateSelection(cell, Clear)

    private fun getClearBoard(): Board {
        val unselectedCells = mutableListOf<Cell>()
        for (row in 0 until boardSideSize) {
            for (column in 0 until boardSideSize) {
                unselectedCells.add(Cell(column, row))
            }
        }
        return Board(unselectedCells)
    }

    /**
     * Removes the cell that changes state if it was not previously selected.
     * Then added it at the end of the list with the new state or at the beginning if
     * the new state is [Clear].
     * This helps to know the last movement.
     */
    private fun updateSelection(cell: Cell, selection: CellState): Result<Unit> {
        boardFlow.value.getCell(cell)?.let { cellToUpdate ->
            if (cellToUpdate.state == selection) {
                return Result.success(Unit)
            }
        }

        with(boardFlow.value.cells.toMutableList()) {
            if (remove(cell)) {
                val selectedCell = cell.copy(state = selection)
                if (selection == Clear) {
                    add(0, selectedCell)
                } else {
                    add(selectedCell)
                }
                boardFlow.value = Board(this)
                return Result.success(Unit)
            }
        }
        return Result.failure(Throwable("The cell selection cannot be changed"))
    }
}
