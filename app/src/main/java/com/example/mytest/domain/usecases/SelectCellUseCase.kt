package com.example.mytest.domain.usecases

import com.example.mytest.app.di.RepositoryModule
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.repositories.BoardRepository
import javax.inject.Inject
import javax.inject.Named

class SelectCellUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository
) {

    suspend operator fun invoke(cell: Cell, player: Player): Result<Unit> {
        if (cell.state !is Clear) {
            return Result.failure(Throwable("The cell is already selected"))
        }
        return boardRepository.getBoard().value.let { board ->
            if (board.cells.contains(cell)) {
                boardRepository.updateCellSelection(cell, player)
            } else {
                Result.failure(Throwable("The cell does not exist in the board"))
            }
        }
    }

}
