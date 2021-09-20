package com.example.mytest.domain.usecases

import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.domain.repositories.BoardRepository
import kotlinx.coroutines.flow.lastOrNull

class GetNextPlayerUseCase(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(): Player =
        boardRepository.getBoard().lastOrNull()?.let { currentBoard ->
            when (currentBoard.cells.last().state) {
                XSelected -> OPlayer
                Clear,
                OSelected -> XPlayer
            }
        } ?: XPlayer
}
