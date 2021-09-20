package com.example.mytest.presentation.board

sealed class ViewStates {
    object Loading : ViewStates()
    object Playing : ViewStates()
    object Finished : ViewStates()
}
