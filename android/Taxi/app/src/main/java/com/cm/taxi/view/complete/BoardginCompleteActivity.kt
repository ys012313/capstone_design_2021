package com.cm.taxi.view.complete

import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivityBoardingTaxiBinding
import com.cm.taxi.state.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class BoardginCompleteActivity : BaseActivity<ActivityBoardingTaxiBinding, BoardginCompleteViewModel>() {

    private val boardNo by lazy { intent.getStringExtra("EXTRA_BOARD_NO") }
    private val boardCompleteDialog by lazy { BoardCompleteDialog.getInstance() }

    override fun getLayoutId(): Int {
        return R.layout.activity_boarding_taxi
    }

    override fun createViewModel(): BoardginCompleteViewModel {
        val viewModel: BoardginCompleteViewModel by viewModel()
        return viewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        L.i(":::boardNo " + boardNo)
        boardNo?.let { viewModel.feachFixBoardingGroup(it) }
        initObserve()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initObserve() {

        with(viewModel) {
            uiState.asLiveData().observe(this@BoardginCompleteActivity) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        closeDialog()
                        setResult(RESULT_OK)
                        finish()
                    }
                    is UiState.Failed -> {
                        closeDialog()
                    }
                }

            }

            event.asLiveData().observe(this@BoardginCompleteActivity) { it ->
                when (it) {
                    is BoardginCompleteViewModel.Event.onShowDialog -> {
                        boardCompleteDialog.arguments = Bundle().apply {
                            putParcelable("KEY_FIX_ITEM", it.data)
                        }
                        boardCompleteDialog.show(supportFragmentManager, boardCompleteDialog.tag)
                    }
                    is BoardginCompleteViewModel.Event.onHideDialog -> {
                        L.i("::::it onHideDialog")
                        closeDialog()
                    }
                }
            }

        }


    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    private fun closeDialog() {
        if (boardCompleteDialog.isAdded && boardCompleteDialog.isVisible) {
            boardCompleteDialog.dismiss()
        }
    }
}