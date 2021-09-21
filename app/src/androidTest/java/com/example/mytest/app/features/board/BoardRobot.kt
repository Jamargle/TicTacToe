package com.example.mytest.app.features.board

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.mytest.R
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import org.hamcrest.CoreMatchers.allOf

inline fun onTurnView(block: BoardRobot.() -> Unit) = BoardRobot().apply(block)

inline fun onBoardView(block: BoardRobot.() -> Unit) = BoardRobot().apply(block)

class BoardRobot {

    fun hasTitleLabel() {
        onView(
            allOf(
                withId(R.id.header),
                withText(R.string.board_screen_title)
            )
        ).check(matches(isDisplayed()))
    }

    fun hasNextTurnWith(player: Player) {
        onView(withId(R.id.next_player_title)).run {
            val playerLabel = when (player) {
                OPlayer -> "X"
                XPlayer -> "O"
            }
            check(
                matches(
                    allOf(
                        withText(playerLabel), isDisplayed()
                    )
                )
            )
        }
    }

    fun hasBoard() {
        onView(withId(R.id.board)).check(matches(isDisplayed()))
    }
}
