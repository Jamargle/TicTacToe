package com.example.tictactoe.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
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
    isEnabled: Boolean,
    onCellClick: (CellUiData) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium.copy(
            all = CornerSize(dimensionResource(id = R.dimen.cell_corner_size))
        ),
        elevation = dimensionResource(id = cellData.state.cellElevation),
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.cell_size))
            .width(dimensionResource(id = R.dimen.cell_size))
            .padding(dimensionResource(id = R.dimen.default_small_space))
            .clickable(
                enabled = isEnabled && cellData.state == Clear,
                onClick = { onCellClick(cellData) }
            )) {
        if (cellData.state != Clear) {
            Image(
                painter = painterResource(id = cellData.state.selectionImage),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                contentDescription = null, // TODO modify content description
                contentScale = ContentScale.Fit
            )
        }
    }
}

// region previews
@Preview("Cell Selected by XPlayer")
@Composable
private fun CellSelectedByX() {
    Cell(
        cellData = CellUiData(0, 0, state = XSelected),
        isEnabled = false,
        onCellClick = {}
    )
}

@Preview("Cell Selected by OPlayer")
@Composable
private fun CellSelectedByO() {
    Cell(
        cellData = CellUiData(0, 0, state = OSelected),
        isEnabled = false,
        onCellClick = {}
    )
}

@Preview("Cell Not Selected")
@Composable
private fun CellNotSelected() {
    Cell(
        cellData = CellUiData(0, 0, state = Clear),
        isEnabled = false,
        onCellClick = {}
    )
}
// endregion
