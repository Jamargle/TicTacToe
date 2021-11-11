package com.example.tictactoe.presentation.model.mappers

import com.example.tictactoe.domain.model.Board
import com.example.tictactoe.domain.model.Cell
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData

fun Board.toBoardData(
    mapToCellUiData: (Cell) -> CellUiData = { it.toCellUiData() }
) = BoardUiData(
    cells = cells.map { mapToCellUiData(it) }
)
