package com.example.tictactoe.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.tictactoe.R
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData

@Composable
fun Board(
    boardSize: Int,
    boardData: BoardUiData,
    onCellClicked: (CellUiData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.default_space))
    ) {
        for (row in 0 until boardSize) {
            Row {
                for (column in 0 until boardSize) {
                    boardData.getCell(row, column)?.let { cell ->
                        Cell(
                            cellData = cell,
                            onCellClick = { onCellClicked(cell) }
                        )
                    }
                }
            }
        }
    }
}
