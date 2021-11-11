package com.example.tictactoe.presentation.board

import com.example.tictactoe.domain.model.Player

sealed class ViewStates {
    object Loading : ViewStates()

    sealed class Playing : ViewStates()
    object XPlaying : Playing()
    object OPlaying : Playing()

    sealed class Finished : ViewStates() {
        object Error : Finished()
        object Draw : Finished()
        class Win(val winner: Player) : Finished()
    }
}
