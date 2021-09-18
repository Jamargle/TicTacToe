package com.example.mytest.domain.model.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.repositories.BoardRepository
import kotlinx.coroutines.flow.Flow

class GetBoardStateUseCase(
    private val gameRepository: BoardRepository
) {

    suspend operator fun invoke(): Flow<Board> =
        gameRepository.getBoard()
}
