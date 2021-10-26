package com.cm.taxi.view.setting

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivityLoginBinding
import com.cm.taxi.databinding.ActivitySettingBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.KeyboardManager
import com.cm.taxi.util.Prefer
import com.cm.taxi.view.complete.BoardCompleteDialog
import com.cm.taxi.view.mate.SearchMateActivity
import com.cm.taxi.view.setting.latest.LatestMateActivity
import com.cm.taxi.view.setting.statistics.StatisticsActivity
import com.cm.taxi.view.signup.SignupActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>() {

    private val userDeleteDialog by lazy { UserDeleteCompleteDialog.getInstance() }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun createViewModel(): SettingViewModel {
        val viewModel: SettingViewModel by viewModel()
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

            navBackPress.observe(this@SettingActivity, EventObserver {
                finish()
            })

            navLatestMateScreen.observe(this@SettingActivity, EventObserver {
                startActivity(Intent(this@SettingActivity, LatestMateActivity::class.java))
            })

            navUseRateScreen.observe(this@SettingActivity, EventObserver {
                startActivity(Intent(this@SettingActivity, StatisticsActivity::class.java))
            })
            navUserDelete.observe(this@SettingActivity, EventObserver {
                userDeleteDialog.show(supportFragmentManager, userDeleteDialog.tag)
            })

            uiState.asLiveData().observe(this@SettingActivity) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        closeDialog()
                    }
                    is UiState.Failed -> {
                        closeDialog()
                    }
                }

            }
        }

    }

    private fun closeDialog() {
        if (userDeleteDialog.isAdded && userDeleteDialog.isVisible) {
            userDeleteDialog.dismiss()
        }
    }
}