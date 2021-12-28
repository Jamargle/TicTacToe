package com.example.tictactoe.presentation.mappers

import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.domain.model.Cell as DomainCell
import com.example.tictactoe.domain.model.Clear as DomainClear
import com.example.tictactoe.domain.model.OSelected as DomainOSelected
import com.example.tictactoe.domain.model.XSelected as DomainXSelected
import com.example.tictactoe.presentation.model.Clear as PresentationClear
import com.example.tictactoe.presentation.model.OSelected as PresentationOSelected
import com.example.tictactoe.presentation.model.XSelected as PresentationXSelected

object CellMapper {

    fun mapToPresentation(
        cell: DomainCell
    ) = CellUiData(
        cell.column,
        cell.row,
        state = when (cell.state) {
            DomainClear -> PresentationClear
            DomainOSelected -> PresentationOSelected
            DomainXSelected -> PresentationXSelected
        }
    )

    fun mapToDomain(cell: CellUiData) = DomainCell(
        cell.column,
        cell.row,
        state = when (cell.state) {
            PresentationClear -> DomainClear
            PresentationOSelected -> DomainOSelected
            PresentationXSelected -> DomainXSelected
        }
    )
}
