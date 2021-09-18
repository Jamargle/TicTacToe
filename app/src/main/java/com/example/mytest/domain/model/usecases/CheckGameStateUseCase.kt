package com.example.mytest.domain.model.usecases

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.GameState
import com.example.mytest.domain.model.repositories.BoardRepository
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

private fun Board.isCompleted() =
    cells.find { it.state == Clear } == null
