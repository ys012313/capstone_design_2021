package com.cm.taxi.view.signup

import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivitySignupBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.KeyboardManager
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_signup
    }

    override fun createViewModel(): SignupViewModel {
        val viewModel: SignupViewModel by viewModel()
        return viewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initObserve()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initObserve() {

        with(viewModel) {
            uiState.asLiveData().observe(this@SignupActivity) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        KeyboardManager.run { hideKeyboard() }
                        finish()
                    }
                    is UiState.Failed -> {
                        KeyboardManager.run { hideKeyboard() }
                    }
                }

            }
        }


    }
}