package com.example.mytest.app.features.board

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.mytest.R
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.utils.isDisplayed
import com.example.mytest.utils.withCompoundDrawable
import com.example.mytest.utils.withText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not

inline fun onTurnView(block: BoardRobot.() -> Unit) = BoardRobot().apply(block)

inline fun onBoardView(block: BoardRobot.() -> Unit) = BoardRobot().apply(block)

class BoardRobot {

    fun hasTitleLabel() {
        onView(
            allOf(
                withId(R.id.header),
                withText(R.string.board_screen_title)
            )
        ).isDisplayed()
    }

    fun hasNextTurnWith(player: Player) {
        onView(withId(R.id.next_player)).run {
            withText(R.string.next_player_title_label)
            when (player) {
                XPlayer -> check(matches(withCompoundDrawable(R.drawable.ic_x)))
                OPlayer -> check(matches(withCompoundDrawable(R.drawable.ic_o)))
            }
            isDisplayed()
        }
    }

    fun hasClearBoard() {
        val cellViews = listOf(
            onView(withId(R.id.cell_1)),
            onView(withId(R.id.cell_2)),
            onView(withId(R.id.cell_3)),
            onView(withId(R.id.cell_4)),
            onView(withId(R.id.cell_5)),
            onView(withId(R.id.cell_6)),
            onView(withId(R.id.cell_7)),
            onView(withId(R.id.cell_8)),
            onView(withId(R.id.cell_9)),
        )
        cellViews.forEach {
            it.isDisplayed()
            it.isNotSelected()
        }
    }

    fun onClickOnCellSelectsItForPlayer(cellId: Int, player: Player) {
        onView(withId(cellId)).perform(click())

        onView(withParent(withId(cellId))).isSelectedForPlayer(player)
    }

    private fun ViewInteraction.isNotSelected() {
        check(
            matches(
                allOf(
                    not(withCompoundDrawable(R.drawable.ic_x)),
                    not(withCompoundDrawable(R.drawable.ic_o))
                )
            )
        )
    }

    private fun ViewInteraction.isSelectedForPlayer(player: Player) {
        val playerSelectionDrawable = when (player) {
            XPlayer -> R.drawable.ic_x
            OPlayer -> R.drawable.ic_o
        }
        check(matches(withTagValue(equalTo(playerSelectionDrawable))))
    }
}
