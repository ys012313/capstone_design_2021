package com.cm.taxi.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<B : ViewDataBinding>(private val layoutId: Int) : BottomSheetDialogFragment() {

    private var _binding: B? = null
    protected val binding: B get() = _binding!!

    fun onCloseClick() {
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding?.lifecycleOwner = this
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}