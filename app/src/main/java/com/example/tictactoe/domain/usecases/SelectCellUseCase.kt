package com.example.tictactoe.domain.usecases

import com.example.tictactoe.app.di.RepositoryModule
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.domain.model.Clear
import com.example.tictactoe.domain.model.OPlayer
import com.example.tictactoe.domain.model.OSelected
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.domain.model.XSelected
import com.example.tictactoe.domain.repositories.BoardRepository
import javax.inject.Inject
import javax.inject.Named

class SelectCellUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository
) {

    suspend operator fun invoke(cell: Cell, player: Player): Result<Unit> =
        boardRepository.getBoard().value.let { board ->
            if (!board.isCellSelected(cell)) {
                Result.failure(IllegalStateException("Cell was already selected"))
            } else {
                val selection = when (player) {
                    OPlayer -> OSelected
                    XPlayer -> XSelected
                }
                boardRepository.updateCellSelection(cell, selection)
            }
        }

    private fun Board.isCellSelected(cell: Cell): Boolean =
        getCell(row = cell.row, column = cell.column)?.state == Clear
}
