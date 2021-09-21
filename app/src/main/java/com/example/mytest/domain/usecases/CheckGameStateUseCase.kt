package com.example.mytest.domain.usecases

import com.example.mytest.app.di.RepositoryModule
import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.repositories.BoardRepository
import com.example.mytest.domain.utils.WinnerCheckHelper
import kotlinx.coroutines.flow.lastOrNull
import javax.inject.Inject
import javax.inject.Named

class CheckGameStateUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository,
    private val winnerCheckHelper: WinnerCheckHelper
) {
    suspend operator fun invoke(): Result<GameState> =
        boardRepository.getBoard().lastOrNull()?.let { board ->
            val gameState = winnerCheckHelper.checkForWinner(board)?.let { winner ->
                GameState.Winner(winner)
            } ?: if (board.isCompleted()) {
                GameState.Draw
            } else {
                GameState.Ongoing
            }
            Result.success(gameState)
        } ?: Result.failure(Throwable("The board is not available"))
}
