package com.example.tictactoe.presentation.model.mappers

import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.domain.model.Cell as DomainCell
import com.example.tictactoe.domain.model.Clear as DomainClear
import com.example.tictactoe.domain.model.OSelected as DomainOSelected
import com.example.tictactoe.domain.model.XSelected as DomainXSelected
import com.example.tictactoe.presentation.model.Clear as PresentationClear
import com.example.tictactoe.presentation.model.OSelected as PresentationOSelected
import com.example.tictactoe.presentation.model.XSelected as PresentationXSelected

fun DomainCell.toCellUiData() = CellUiData(
    column,
    row,
    state = when (state) {
        DomainClear -> PresentationClear
        DomainOSelected -> PresentationOSelected
        DomainXSelected -> PresentationXSelected
    }
)

fun CellUiData.toCell() = DomainCell(
    column,
    row,
    state = when (state) {
        PresentationClear -> DomainClear
        PresentationOSelected -> DomainOSelected
        PresentationXSelected -> DomainXSelected
    }
)
