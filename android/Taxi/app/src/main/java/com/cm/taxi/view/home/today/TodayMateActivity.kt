package com.cm.taxi.view.home.today

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivityTodayTaxiTogetherBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.refresh
import com.cm.taxi.view.complete.BoardginCompleteActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
internal class TodayMateActivity : BaseActivity<ActivityTodayTaxiTogetherBinding, TodayMateViewModel>() {

    private val taxiMateAdapter by lazy { TodayAdapter(viewModel) }

    override fun getLayoutId(): Int {
        return R.layout.activity_today_taxi_together
    }

    override fun createViewModel(): TodayMateViewModel {
        val viewMateViewModel: TodayMateViewModel by viewModel()
        return viewMateViewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initRecyclerView()
        initObserve()
        initReFresh()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initRecyclerView() {
        binding.rvTaxiMate.run {
            layoutManager = LinearLayoutManager(context)
            adapter = taxiMateAdapter
        }

    }

    private fun initReFresh() {
        binding.swipeRefresh.refresh().map {
            viewModel.feachTodayTaxiMate()
        }.launchIn(lifecycleScope)
    }


    private fun initObserve() {
        with(viewModel) {

            navBoaringScreen.observe(this@TodayMateActivity, EventObserver {
                TedRxOnActivityResult.with(this@TodayMateActivity)
                    .startActivityForResult(
                        Intent(this@TodayMateActivity, BoardginCompleteActivity::class.java).apply {
                            putExtra("EXTRA_BOARD_NO", it.boarding.boardNo.toString())
                        })
                    .subscribe { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            viewModel.feachTodayTaxiMate()
                        }
                    }
            })


            uiState.asLiveData().observe(this@TodayMateActivity) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        taxiMateAdapter.submitList(it.data)
                    }
                    is UiState.Failed -> {
                        binding.swipeRefresh.isRefreshing = false
                    }
                }

            }

        }


    }
}