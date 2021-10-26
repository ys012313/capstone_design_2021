package com.cm.taxi.base

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cm.taxi.util.initLazy

abstract class BaseActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel>() : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewModel(): ViewModel

    abstract fun loadState(bundle: Bundle)
    abstract fun saveState(bundle: Bundle)

    val binding: Binding by lazy { DataBindingUtil.setContentView(this, getLayoutId()) as Binding }
    val viewModel: ViewModel by lazy { createViewModel() }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            savedInstanceState != null -> loadState(savedInstanceState)
            intent.extras != null -> loadState(intent.extras!!)
            else -> loadState(Bundle.EMPTY)
        }

        binding.initLazy()
        viewModel.initLazy()

        binding.lifecycleOwner = this

        viewModel.toast.observe(this, { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    fun showStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun hideStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

//    fun showLoading() = loadingDialog.show()
//
//    fun hideLoading() = loadingDialog.dismiss()


}