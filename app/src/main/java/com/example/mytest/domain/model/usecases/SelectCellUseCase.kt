package com.example.mytest.domain.model.usecases

import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.repositories.BoardRepository
import kotlinx.coroutines.flow.lastOrNull

class SelectCellUseCase(
    private val boardRepository: BoardRepository
) {

    suspend operator fun invoke(cell: Cell, player: Player): Result<Unit> {
        if (cell.state !is Clear) {
            return Result.failure(Throwable("The cell is already selected"))
        }
        return boardRepository.getBoard().lastOrNull()?.let { board ->
            if (board.cells.contains(cell)) {
                boardRepository.updateCellSelection(cell, player)
            } else {
                Result.failure(Throwable("The cell does not exist in the board"))
            }
        } ?: Result.failure(Throwable("The board is not available"))
    }

}
