package com.example.tictactoe.domain.model

data class Cell(
    val column: Int,
    val row: Int,
    val state: CellState = Clear
)
