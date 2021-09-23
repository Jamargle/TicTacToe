package com.example.mytest.domain.model

data class Board(
    val cells: List<Cell>
) {
    fun isCompleted() = cells.find { it.state == Clear } == null

    fun getCell(row: Int, column: Int): Cell? =
        cells.find { it.row == row && it.column == column }

    fun getCell(cell: Cell): Cell? = getCell(cell.row, cell.column)
}
