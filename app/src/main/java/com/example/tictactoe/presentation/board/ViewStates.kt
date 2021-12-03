package com.example.tictactoe.presentation.board

import com.example.tictactoe.R

sealed class ViewStates(
    val gameStateLabelRes: Int
) {
    object Loading : ViewStates(R.string.game_state_loading)

    sealed class Playing(gameStateLabelRes: Int) : ViewStates(gameStateLabelRes)
    object XPlaying : Playing(R.string.next_player_title_label_X)
    object OPlaying : Playing(R.string.next_player_title_label_O)

    sealed class Finished(gameStateLabelRes: Int) : ViewStates(gameStateLabelRes) {
        object Error : Finished(R.string.general_error_message)
        object Draw : Finished(R.string.game_finish_with_draw)
        sealed class Win(gameStateLabelRes: Int) : Finished(gameStateLabelRes) {
            object OPlayerWins : Win(R.string.game_finish_with_winner_o_player)
            object XPlayerWins : Win(R.string.game_finish_with_winner_x_player)
        }
    }
}
