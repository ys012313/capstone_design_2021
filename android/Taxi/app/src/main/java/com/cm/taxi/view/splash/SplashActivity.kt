package com.cm.taxi.view.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.cm.taxi.L
import com.cm.taxi.MainActivity
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivitySplashBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun createViewModel(): SplashViewModel {
        val viewModel: SplashViewModel by viewModel()
        return viewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initObserve()
        initAnimation()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initAnimation() {
        val animator = ValueAnimator.ofFloat(0f,1.0f).setDuration(3000)

        animator.run {
            addUpdateListener {
                binding.lottie.progress = it.animatedValue as Float
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    viewModel.nextScreen()
                }

                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    binding.logo.visibility = View.VISIBLE
                }

            })
            start()
        }

    }


    private fun initObserve() {
        with(viewModel) {
            navMainSrceen.observe(this@SplashActivity, EventObserver {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            })
        }
    }
}