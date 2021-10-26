package com.cm.taxi.view.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivityLoginBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.KeyboardManager
import com.cm.taxi.util.Prefer
import com.cm.taxi.view.signup.SignupActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun createViewModel(): LoginViewModel {
        val viewModel: LoginViewModel by viewModel()
        return viewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initObserve()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initObserve() {
        L.e("::::::::::::::::::::::: 테스트 " + Prefer.get(Prefer.KEY_USER_ID, ""))

        with(viewModel) {
            uiState.asLiveData().observe(this@LoginActivity) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        KeyboardManager.run { hideKeyboard() }
                        Prefer.set(Prefer.KEY_USER_ID, it.data)
                        finish()
                    }
                    is UiState.Failed -> {
                        KeyboardManager.run { hideKeyboard() }
                    }

                }

            }

            navSignUpSrceen.observe(this@LoginActivity, EventObserver {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            })
        }


    }
}