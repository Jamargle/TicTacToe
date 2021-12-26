package com.example.tictactoe.presentation.mappers

import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.domain.model.Board as DomainBoard
import com.example.tictactoe.domain.model.Cell as DomainCell

object BoardMapper {

    fun mapToPresentation(
        board: DomainBoard,
        mapCellToPresentation: (DomainCell) -> CellUiData = { CellMapper.mapToPresentation(it) }
    ) = BoardUiData(
        cells = board.cells.map { mapCellToPresentation(it) }
    )
}
