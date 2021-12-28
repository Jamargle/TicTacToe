package com.example.tictactoe.presentation.model

import com.example.tictactoe.R

class CellUiData(
    val column: Int,
    val row: Int,
    val state: CellState = Clear
)

sealed interface CellState {
    val selectionImage: Int
    val cellElevation: Int
}

object Clear : CellState {
    override val selectionImage: Int = -1
    override val cellElevation: Int = R.dimen.clear_cell_elevation
}

object XSelected : CellState {
    override val selectionImage: Int = R.drawable.ic_x
    override val cellElevation: Int = R.dimen.selected_cell_elevation
}

object OSelected : CellState {
    override val selectionImage: Int = R.drawable.ic_o
    override val cellElevation: Int = R.dimen.selected_cell_elevation
}
