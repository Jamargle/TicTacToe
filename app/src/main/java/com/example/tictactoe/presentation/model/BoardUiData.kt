package com.example.tictactoe.presentation.model

class BoardUiData(
    val cells: List<CellUiData>
) {
    fun getCell(row: Int, column: Int) =
        cells.find { it.row == row && it.column == column }
}
