package com.example.mytest.domain.model

data class Cell(
    val column: Int,
    val row: Int,
    val state: CellState = Clear
)
