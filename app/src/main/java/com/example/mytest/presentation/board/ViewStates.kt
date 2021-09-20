package com.example.mytest.presentation.board

import com.example.mytest.domain.model.Player

sealed class ViewStates {
    object Loading : ViewStates()
    object Playing : ViewStates()
    sealed class Finished : ViewStates() {
        object Draw : Finished()
        class Win(val winner: Player) : Finished()
    }
}
