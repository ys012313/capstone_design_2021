package com.cm.taxi.view.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.cm.taxi.R
import com.cm.taxi.base.BaseBottomSheetDialogFragment
import com.cm.taxi.databinding.UserDeleteBottomSheetDialogBinding
import com.cm.taxi.util.setOnShortBlockClick
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserDeleteCompleteDialog :
    BaseBottomSheetDialogFragment<UserDeleteBottomSheetDialogBinding>(R.layout.user_delete_bottom_sheet_dialog) {


    private val viewModel by sharedViewModel<SettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        renderView()
    }

    private fun renderView() {
        with(binding) {
            btnOne.setOnShortBlockClick {
                dismiss()
            }
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: UserDeleteCompleteDialog? = null
        fun getInstance(): UserDeleteCompleteDialog = INSTANCE ?: UserDeleteCompleteDialog().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
            isCancelable = false
        }
    }
}