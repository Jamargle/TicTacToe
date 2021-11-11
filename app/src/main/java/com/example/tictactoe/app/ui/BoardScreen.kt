package com.example.tictactoe.app.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe.R
import com.example.tictactoe.presentation.model.BoardUiData
import com.example.tictactoe.presentation.model.CellUiData

@Composable
fun BoardScreen(
    boardSize: Int,
    gameStateLabel: String,
    boardData: BoardUiData,
    onCellClicked: (CellUiData) -> Unit,
    onRestartButtonClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(state = scrollState)
    ) {
        Header()
        GameStateLabel(gameStateLabel)
        Board(
            boardSize = boardSize,
            boardData = boardData,
            onCellClicked = onCellClicked,
            modifier = Modifier.align(alignment = CenterHorizontally)
        )
        Spacer(modifier = Modifier.weight(1f))
        RestartButton(
            modifier = Modifier.align(alignment = CenterHorizontally),
            onRestartButtonClicked = onRestartButtonClicked
        )
    }
}

@Composable
private fun Header() {
    Text(
        text = stringResource(id = R.string.board_screen_title),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_space))
    )
}

@Composable
private fun GameStateLabel(gameStateLabel: String) {
    Text(
        text = gameStateLabel,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_space))
    )
}

@Composable
private fun RestartButton(
    modifier: Modifier = Modifier,
    onRestartButtonClicked: () -> Unit
) {
    Button(
        modifier = modifier.padding(dimensionResource(id = R.dimen.default_space)),
        onClick = onRestartButtonClicked
    ) {
        Text(text = stringResource(id = R.string.restart_game_button))
    }
}

@Preview(
    "Board Screen Light mode",
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.NEXUS_5X,
    showSystemUi = true
)
@Composable
private fun BoardScreenLight() {
    BoardScreen(
        boardSize = 3,
        gameStateLabel = "Game state will be HERE",
        boardData = BoardUiData(emptyList()),
        onCellClicked = {},
        onRestartButtonClicked = {}
    )
}
