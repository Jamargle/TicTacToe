package com.example.mytest.domain.model.usecases

import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.model.repositories.BoardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckGameStateUseCase(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(): Result<GameState> {
        return withContext(Dispatchers.IO) {
            Result.failure(Throwable(""))
        }
    }
}
