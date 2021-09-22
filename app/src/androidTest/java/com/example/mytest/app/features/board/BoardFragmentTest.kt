package com.example.mytest.app.features.board

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mytest.R
import com.example.mytest.app.MainActivity
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.XPlayer
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoardFragmentTest {

    @get:Rule
    val activityScenarioTestRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun tearDown() {
        onBoardView { onResetGameRestartsTheBoard() }
        onTurnView { hasNextTurnWith(XPlayer) }
    }

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
            hasClearBoard()
        }
    }

    @Test
    fun onSelectCellsUpdatesThemForEachCurrentPlayerUntilCompletion() {
        activityScenarioTestRule.scenario.run {
            moveToState(Lifecycle.State.RESUMED)

            onTurnView {
                hasNextTurnWith(XPlayer)
            }
            onBoardView {
                hasClearBoard()
            }
        }

        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_9, XPlayer) }

        onTurnView { hasNextTurnWith(OPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_8, OPlayer) }

        onTurnView { hasNextTurnWith(XPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_7, XPlayer) }

        onTurnView { hasNextTurnWith(OPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_6, OPlayer) }

        onTurnView { hasNextTurnWith(XPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_5, XPlayer) }

        onTurnView { hasNextTurnWith(OPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_3, OPlayer) }

        onTurnView { hasNextTurnWith(XPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_2, XPlayer) }

        onTurnView { hasNextTurnWith(OPlayer) }
        onBoardView { onClickOnCellSelectsItForPlayer(R.id.cell_1, OPlayer) }

        onTurnView { hasNextTurnWith(XPlayer) }
        onBoardView {
            clickOnCell(R.id.cell_4)
            gameStateIsDraw()
        }
        onTurnView { hasNotTurnView() }
    }

    @Test
    fun onSelectCellsUpdatesThemForEachCurrentPlayerUntilXWins() {
        activityScenarioTestRule.scenario.run {
            moveToState(Lifecycle.State.RESUMED)
        }

        onBoardView {
            onClickOnCellSelectsItForPlayer(R.id.cell_1, XPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_4, OPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_2, XPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_5, OPlayer)
            clickOnCell(R.id.cell_3)

            gameStateIsWinFor(XPlayer)
        }
    }

    @Test
    fun onSelectCellsUpdatesThemForEachCurrentPlayerUntilOWins() {
        activityScenarioTestRule.scenario.run {
            moveToState(Lifecycle.State.RESUMED)
        }

        onBoardView {
            onClickOnCellSelectsItForPlayer(R.id.cell_1, XPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_4, OPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_2, XPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_5, OPlayer)
            onClickOnCellSelectsItForPlayer(R.id.cell_9, XPlayer)
            clickOnCell(R.id.cell_6)

            gameStateIsWinFor(OPlayer)
        }
    }

}
