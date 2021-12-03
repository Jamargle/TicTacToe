package com.example.tictactoe.domain.usecases

import com.example.tictactoe.app.di.RepositoryModule
import com.example.tictactoe.domain.model.GameState
import com.example.tictactoe.domain.repositories.BoardRepository
import com.example.tictactoe.domain.utils.WinnerCheckHelper
import javax.inject.Inject
import javax.inject.Named

class CheckGameStateUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository,
    private val winnerCheckHelper: WinnerCheckHelper
) {
    suspend operator fun invoke(): Result<GameState> =
        boardRepository.getBoard().value.let { board ->
            val gameState = winnerCheckHelper.checkForWinner(board)?.let { winner ->
                GameState.Winner(winner)
            } ?: if (board.isCompleted()) {
                GameState.Draw
            } else {
                GameState.Ongoing
            }
            Result.success(gameState)
        }
}
