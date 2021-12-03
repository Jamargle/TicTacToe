package com.example.tictactoe.domain.usecases

import com.example.tictactoe.app.di.RepositoryModule
import com.example.tictactoe.domain.model.Clear
import com.example.tictactoe.domain.model.OPlayer
import com.example.tictactoe.domain.model.OSelected
import com.example.tictactoe.domain.model.Player
import com.example.tictactoe.domain.model.XPlayer
import com.example.tictactoe.domain.model.XSelected
import com.example.tictactoe.domain.repositories.BoardRepository
import javax.inject.Inject
import javax.inject.Named

class GetNextPlayerUseCase
@Inject constructor(
    @Named(RepositoryModule.ON_MEMORY_BOARD_REPOSITORY) private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(): Player =
        boardRepository.getBoard().value.let { currentBoard ->
            when (currentBoard.cells.last().state) {
                XSelected -> OPlayer
                Clear,
                OSelected -> XPlayer
            }
        }
}
