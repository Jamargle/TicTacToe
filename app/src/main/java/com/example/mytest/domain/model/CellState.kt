package com.example.mytest.domain.model

sealed interface CellState

object Clear : CellState

object XSelected : CellState

object OSelected : CellState
