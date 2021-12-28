package com.example.tictactoe.utils

import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.presentation.model.Clear

internal fun getCellUiData(boardSize: Int): List<CellUiData> {
    val cellsData = mutableListOf<CellUiData>()
    for (row in 0 until boardSize) {
        for (column in 0 until boardSize) {
            cellsData.add(CellUiData(column = column, row = row, state = Clear))
        }
    }
    return cellsData
}
