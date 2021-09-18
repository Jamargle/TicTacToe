package com.example.mytest.domain.utils

import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Player
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WinnerCheckHelper(
    private val backgroundDispatcher: CoroutineDispatcher
) {

    suspend fun checkForWinner(board: Board): Player? {
        return withContext(backgroundDispatcher) {
            null
        }
    }
}
