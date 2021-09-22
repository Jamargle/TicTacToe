package com.example.mytest.domain.usecases

import com.example.mytest.app.di.RepositoryModule
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.repositories.BoardRepository
import javax.inject.Inject
import javax.inject.Named

class UnselectCellUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository
) {

    operator fun invoke(cell: Cell): Result<Unit> {
        if (cell.state == Clear) {
            return Result.success(Unit)
        }

        boardRepository.clearCellSelection(cell).fold(
            onSuccess = { return Result.success(Unit) },
            onFailure = { return Result.failure(it) }
        )
    }

}
