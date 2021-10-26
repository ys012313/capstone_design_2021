package com.cm.taxi.view.home

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.cm.taxi.L
import com.cm.taxi.MainActivity
import com.cm.taxi.R
import com.cm.taxi.base.BaseFragment
import com.cm.taxi.databinding.FragmentHomeBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.refresh
import com.cm.taxi.view.complete.BoardginCompleteActivity
import com.cm.taxi.view.home.today.TodayMateActivity
import com.cm.taxi.view.login.LoginActivity
import com.cm.taxi.view.mate.SearchMateActivity
import com.cm.taxi.view.setting.SettingActivity
import com.cm.taxi.view.signup.SignupActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import org.koin.android.ext.android.inject


@FlowPreview
@ExperimentalCoroutinesApi
internal class HomeFragment : BaseFragment<FragmentHomeBinding,
        HomeViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun createViewModel(): HomeViewModel {
        val viewModel: HomeViewModel by inject()
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel


        initAnimate()
        initObserve()
        initReFresh()

    }

    private fun initAnimate() {
        binding.start.animate().alpha(1f).setDuration(500).setStartDelay(800).withStartAction {
            binding.start.visibility = View.VISIBLE
        }.withEndAction {}.start()
    }


    private fun initReFresh() {
        binding.swipeRefresh.refresh().map {
            todayBadgeAnimation(false)
            viewModel.feachTodayBoardingCnt()
        }.launchIn(lifecycleScope)
    }


    private fun initObserve() {
        with(viewModel) {
            navSearchMate.observe(viewLifecycleOwner, EventObserver {
                TedRxOnActivityResult.with(requireContext())
                    .startActivityForResult(Intent(requireContext(), SearchMateActivity::class.java))
                    .subscribe { _ -> viewModel.feachTodayBoardingCnt() }
            })
            navSettingSrceen.observe(viewLifecycleOwner, EventObserver {
                startActivity(Intent(requireContext(), SettingActivity::class.java))
            })
            navLoginSrceen.observe(viewLifecycleOwner, EventObserver {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            })
            navSignUpSrceen.observe(viewLifecycleOwner, EventObserver {
                startActivity(Intent(requireContext(), SignupActivity::class.java))
            })

            navTodayMateSrceen.observe(viewLifecycleOwner, EventObserver {
                TedRxOnActivityResult.with(requireContext())
                    .startActivityForResult(Intent(requireContext(), TodayMateActivity::class.java))
                    .subscribe { _ -> viewModel.feachTodayBoardingCnt() }
            })


            uiState.asLiveData().observe(viewLifecycleOwner) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        todayBadgeAnimation(true)
                        binding.icNotiAmount.badgeValue = it.data
                    }
                    is UiState.Failed -> {
                        binding.swipeRefresh.isRefreshing = false
                    }
                }

            }
        }
    }

    private fun todayBadgeAnimation(isStart: Boolean) {
        if (isStart) {
            binding.icNotiAmount.animate().alpha(1f).scaleX(1.5f).scaleY(1.5f).setDuration(500L).start()
        } else {
            binding.icNotiAmount.animate().alpha(0.4f).scaleX(1f).scaleY(1f).setDuration(500L).start()
        }

    }
}