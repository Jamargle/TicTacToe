package com.example.mytest.domain.repositories

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Player
import kotlinx.coroutines.flow.StateFlow

interface BoardRepository {

    suspend fun getBoard(): StateFlow<Board>

    fun updateCellSelection(cell: Cell, player: Player): Result<Unit>

}
