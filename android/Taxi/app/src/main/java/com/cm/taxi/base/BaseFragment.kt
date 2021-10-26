package com.cm.taxi.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer


abstract class BaseFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel> : Fragment() {

    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun createViewModel(): ViewModel

    private var _binding: Binding? = null
    protected val binding: Binding get() = _binding!!

    protected open val viewModel: ViewModel by lazy { createViewModel() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        _binding?.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toast.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}