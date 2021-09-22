package com.example.mytest.domain.usecases

import com.example.mytest.app.di.RepositoryModule
import com.example.mytest.domain.repositories.BoardRepository
import javax.inject.Inject
import javax.inject.Named

class ClearBoardUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository,
    private val unselectCell: UnselectCellUseCase
) {

    suspend operator fun invoke(): Result<Unit> {
        var error: Throwable? = null
        boardRepository.getBoard().value.cells.forEach { cell ->
            error = unselectCell(cell).exceptionOrNull()
        }
        return error?.let {
            Result.failure(it)
        } ?: Result.success(Unit)
    }

}
