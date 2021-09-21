package com.example.mytest.app.features.board

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mytest.R
import com.example.mytest.app.CustomApplication
import com.example.mytest.app.di.BoardViewModelFactory
import com.example.mytest.databinding.FragmentBoardBinding
import com.example.mytest.presentation.board.BoardViewModel
import javax.inject.Inject

class BoardFragment : Fragment(R.layout.fragment_board) {

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var boardViewModelFactory: BoardViewModelFactory

    private val boardViewModel by viewModels<BoardViewModel> { boardViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initApplicationComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBoardBinding.bind(view)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initApplicationComponent() {
        (requireContext().applicationContext as CustomApplication)
            .appComponent
            .inject(this)
    }
}
