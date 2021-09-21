package com.example.mytest.app.features.board

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mytest.app.MainActivity
import com.example.mytest.domain.model.XPlayer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoardFragmentTest {

    @get:Rule
    val activityScenarioTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun displayBoardScreen() {
        activityScenarioTestRule.scenario.run {
            moveToState(Lifecycle.State.RESUMED)
        }

        onTurnView {
            hasTitleLabel()
            hasNextTurnWith(XPlayer)
        }

        onBoardView {
            hasBoard()
        }
    }
}
