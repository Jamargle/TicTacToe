package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.repositories.BoardRepository
import com.example.mytest.domain.utils.WinnerCheckHelper
import kotlinx.coroutines.flow.lastOrNull

class CheckGameStateUseCase(
    private val boardRepository: BoardRepository,
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
