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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class OnMemoryBoardRepository
@Inject constructor(
    @Named(BOARD_SIZE) private val boardSideSize: Int
) : BoardRepository {

    private var boardFlow: MutableStateFlow<Board> = MutableStateFlow(getClearBoard())

    override suspend fun getBoard(): Flow<Board> = boardFlow

    override fun updateCellSelection(cell: Cell, player: Player): Result<Unit> {
        if (!cell.isClearInBoard()) {
            return Result.failure(IllegalArgumentException("Cell was already selected"))
        } else {
            applySelectedStateForPlayer(cell, player)
        }
        return Result.success(Unit)
    }

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
    private fun applySelectedStateForPlayer(cell: Cell, player: Player) {
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
            }
        }
    }
}
