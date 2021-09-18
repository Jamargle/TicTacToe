package com.example.mytest.domain.model

sealed class GameState {
    object Ongoing : GameState()
    object Draw : GameState()
    class Winner(val winner: Player) : GameState()
}
