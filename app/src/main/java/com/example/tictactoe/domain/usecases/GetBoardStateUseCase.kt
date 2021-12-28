package com.example.tictactoe.domain.usecases

import com.example.tictactoe.app.di.RepositoryModule
import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.repositories.BoardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class GetBoardStateUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val gameRepository: BoardRepository
) {

    suspend operator fun invoke(): Flow<Board> =
        gameRepository.getBoard()
}
