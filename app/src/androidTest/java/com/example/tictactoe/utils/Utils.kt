package com.example.tictactoe.utils

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import com.example.tictactoe.presentation.model.CellUiData
import com.example.tictactoe.presentation.model.Clear
import org.hamcrest.CoreMatchers.not

internal fun ViewInteraction.isDisplayed() = check(matches(ViewMatchers.isDisplayed()))

internal fun ViewInteraction.isClickable() = check(matches(ViewMatchers.isClickable()))

internal fun ViewInteraction.isNotClickable() = check(matches(not(ViewMatchers.isClickable())))

internal fun ViewInteraction.withText(stringRes: Int) =
    check(matches(ViewMatchers.withText(stringRes)))

internal fun getCellUiData(boardSize: Int): List<CellUiData> {
    val cellsData = mutableListOf<CellUiData>()
    for (row in 0 until boardSize) {
        for (column in 0 until boardSize) {
            cellsData.add(CellUiData(column = column, row = row, state = Clear))
        }
    }
    return cellsData
}
