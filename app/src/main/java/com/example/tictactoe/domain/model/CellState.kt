package com.example.tictactoe.domain.model

sealed interface CellState

object Clear : CellState

object XSelected : CellState

object OSelected : CellState
