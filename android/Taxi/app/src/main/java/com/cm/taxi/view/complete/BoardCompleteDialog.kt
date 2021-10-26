package com.cm.taxi.view.complete

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseBottomSheetDialogFragment
import com.cm.taxi.data.remote.model.data.GetFixBoardingGroupResponse
import com.cm.taxi.databinding.BoardingCompleteBottomSheetDialogBinding
import com.cm.taxi.util.getParcelableExtras
import com.cm.taxi.util.setOnShortBlockClick
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BoardCompleteDialog :
    BaseBottomSheetDialogFragment<BoardingCompleteBottomSheetDialogBinding>(R.layout.boarding_complete_bottom_sheet_dialog) {

    private val selectedFixItem: GetFixBoardingGroupResponse? by getParcelableExtras("KEY_FIX_ITEM")
    private val boardViewModel by sharedViewModel<BoardginCompleteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = boardViewModel
        boardViewModel.setFixForm(selectedFixItem)
        renderView()
    }

    private fun renderView() {
        with(binding) {
            btnTwo.setOnShortBlockClick {
                val pay = binding.editId.text?.toString()
                val dutchPay = binding.etDividCost.text?.toString()
                val bankName = binding.etBankName.text?.toString()
                val bankNo = binding.etBankNumber.text?.toString()
                boardViewModel.onClickApplyBottomDialog(pay,dutchPay,bankName,bankNo)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BoardCompleteDialog? = null
        fun getInstance(): BoardCompleteDialog = INSTANCE ?: BoardCompleteDialog().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
            isCancelable = false
        }
    }
}