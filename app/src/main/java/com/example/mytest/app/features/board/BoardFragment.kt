package com.example.mytest.app.features.board

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mytest.R
import com.example.mytest.app.CustomApplication
import com.example.mytest.app.di.BoardViewModelFactory
import com.example.mytest.databinding.FragmentBoardBinding
import com.example.mytest.domain.model.Board
import com.example.mytest.domain.model.Cell
import com.example.mytest.domain.model.CellState
import com.example.mytest.domain.model.Clear
import com.example.mytest.domain.model.OPlayer
import com.example.mytest.domain.model.OSelected
import com.example.mytest.domain.model.Player
import com.example.mytest.domain.model.XPlayer
import com.example.mytest.domain.model.XSelected
import com.example.mytest.presentation.board.BoardViewModel
import com.example.mytest.presentation.board.ViewStates
import com.google.android.material.card.MaterialCardView
import javax.inject.Inject

class BoardFragment : Fragment(R.layout.fragment_board) {

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var boardViewModelFactory: BoardViewModelFactory

    private val boardViewModel by viewModels<BoardViewModel> { boardViewModelFactory }

    // region view state observers
    private val onPlayerTurnChange = Observer<Player> {
        val playerDrawable = when (it) {
            XPlayer -> R.drawable.ic_x
            OPlayer -> R.drawable.ic_o
        }
        binding.nextPlayer.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            ResourcesCompat.getDrawable(resources, playerDrawable, context?.theme),
            null
        )
    }
    private val onBoardStateChange = Observer<Board> {
        updateBoard(it)
    }
    private val onViewStateChange = Observer<ViewStates> {
        when (it) {
            ViewStates.Finished.Draw -> {
                Toast.makeText(context, "THIS IS A DRAW!!!", Toast.LENGTH_LONG).show()
            }
            is ViewStates.Finished.Win -> {
                Toast.makeText(context, "${it.winner} wins!!!", Toast.LENGTH_LONG).show()
            }
            ViewStates.Loading -> binding.loadingView.visibility = View.VISIBLE
            ViewStates.Playing -> {
                // NO -OP
            }
        }
        if (it !is ViewStates.Loading) {
            binding.loadingView.visibility = View.GONE
        }
    }
    // endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initApplicationComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBoardBinding.bind(view)
        initViewStateObservers()
        initCellClickListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initViewStateObservers() {
        with(boardViewModel) {
            getPlayerTurnState().observe(viewLifecycleOwner, onPlayerTurnChange)
            getBoardState().observe(viewLifecycleOwner, onBoardStateChange)
            getViewState().observe(viewLifecycleOwner, onViewStateChange)
        }
    }

    private fun initCellClickListeners() {
        binding.cell1.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell2.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell3.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell4.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell5.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell6.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell7.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell8.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
        binding.cell9.setOnClickListener { boardViewModel.onCellClicked(it.tag as Cell) }
    }

    private fun updateBoard(board: Board) {
        board.cells.forEach {
            val cellView = when {
                it.row == 0 && it.column == 0 -> binding.cell1
                it.row == 0 && it.column == 1 -> binding.cell2
                it.row == 0 && it.column == 2 -> binding.cell3
                it.row == 1 && it.column == 0 -> binding.cell4
                it.row == 1 && it.column == 1 -> binding.cell5
                it.row == 1 && it.column == 2 -> binding.cell6
                it.row == 2 && it.column == 0 -> binding.cell7
                it.row == 2 && it.column == 1 -> binding.cell8
                it.row == 2 && it.column == 2 -> binding.cell9
                else -> null
            }
            if (cellView != null) {
                setCellStateFor(cellView, it)
            }
        }
    }

    private fun setCellStateFor(cellView: MaterialCardView, cell: Cell) {
        cellView.cardElevation = getElevationForState(cell.state)

        cellView.tag = cell
        with(cellView.children.first() as ImageView) {
            setImageResource(getDrawableForState(cell.state))
        }
    }

    private fun getElevationForState(state: CellState): Float {
        val elevationDimension = when (state) {
            Clear -> R.dimen.cell_elevation
            OSelected -> R.dimen.cell_elevation
            XSelected -> R.dimen.cell_elevation
        }
        return resources.getDimension(elevationDimension)
    }

    private fun getDrawableForState(state: CellState) = when (state) {
        OSelected -> R.drawable.ic_o
        XSelected -> R.drawable.ic_x
        Clear -> 0
    }

    private fun initApplicationComponent() {
        (requireContext().applicationContext as CustomApplication)
            .appComponent
            .inject(this)
    }
}
