package com.example.mytest.domain.model

data class Board(
    val cells: List<Cell>
) {
    fun isCompleted() = cells.find { it.state == Clear } == null
}
