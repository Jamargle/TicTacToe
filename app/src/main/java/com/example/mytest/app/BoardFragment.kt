package com.example.mytest.app

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytest.R
import com.example.mytest.app.di.BoardViewModelFactory
import com.example.mytest.presentation.board.BoardViewModel
import javax.inject.Inject

class BoardFragment : Fragment(R.layout.fragment_board) {

    @Inject
    lateinit var boardViewModelFactory: BoardViewModelFactory

    private val boardViewModel by viewModels<BoardViewModel> { boardViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initApplicationComponent()
    }

    private fun initApplicationComponent() {
        (requireContext().applicationContext as CustomApplication)
            .appComponent
            .inject(this)
    }
}
