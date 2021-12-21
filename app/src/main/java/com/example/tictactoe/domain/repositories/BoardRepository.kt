package com.example.tictactoe.domain.repositories

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.CellState
import kotlinx.coroutines.flow.StateFlow

interface BoardRepository {

    suspend fun getBoard(): StateFlow<Board>

    fun updateCellSelection(cell: Cell, selection: CellState): Result<Unit>

    fun clearCellSelection(cell: Cell): Result<Unit>

}
