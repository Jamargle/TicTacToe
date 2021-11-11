package com.example.tictactoe.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe.R
import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.presentation.model.Clear
import com.example.tictactoe.presentation.model.OSelected
import com.example.tictactoe.presentation.model.XSelected

@Composable
fun Cell(
    cellData: CellUiData,
    onCellClick: (CellUiData) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = dimensionResource(id = cellData.state.cellElevation),
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.cell_size))
            .width(dimensionResource(id = R.dimen.cell_size))
            .padding(dimensionResource(id = R.dimen.default_small_space))
            .clickable(
                enabled = cellData.state == Clear,
                onClick = { onCellClick(cellData) }
            )
    ) {
        if (cellData.state != Clear) {
            Image(
                painter = painterResource(id = cellData.state.selectionImage),
                contentDescription = null, // TODO modify content description
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview("Cell Selected by XPlayer")
@Composable
private fun CellSelectedByX() {
    Cell(
        cellData = CellUiData(0, 0, state = XSelected),
        onCellClick = {}
    )
}

@Preview("Cell Selected by OPlayer")
@Composable
private fun CellSelectedByO() {
    Cell(
        cellData = CellUiData(0, 0, state = OSelected),
        onCellClick = {}
    )
}

@Preview("Cell Not Selected")
@Composable
private fun CellNotSelected() {
    Cell(
        cellData = CellUiData(0, 0, state = Clear),
        onCellClick = {}
    )
}
