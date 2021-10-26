package com.cm.taxi.view.setting.latest

import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivityLatestMateBinding
import com.cm.taxi.databinding.ActivityStatisticsBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.refresh
import com.cm.taxi.view.mate.SearchAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
internal class LatestMateActivity : BaseActivity<ActivityLatestMateBinding, LatestViewModel>() {

    private val latestAdapter by lazy { LatestAdapter(viewModel) }

    override fun getLayoutId(): Int {
        return R.layout.activity_latest_mate
    }

    override fun createViewModel(): LatestViewModel {
        val viewModel: LatestViewModel by viewModel()
        return viewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initReFresh()
        initRecyclerView()
        initObserve()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initReFresh() {
        binding.swipeRefresh.refresh().map {
            viewModel.feachLatestMate()
        }.launchIn(lifecycleScope)
    }

    private fun initRecyclerView() {
        binding.rvLatest.run {
            layoutManager = LinearLayoutManager(context)
            adapter = latestAdapter
        }

    }

    private fun initObserve() {

        with(viewModel) {
            uiState.asLiveData().observe(this@LatestMateActivity) { it ->
                L.i(":::it " + it)
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        latestAdapter.submitList(it.data)
                    }
                    is UiState.Failed -> {
                        binding.swipeRefresh.isRefreshing = false
                    }
                }

            }


            blackListState.asLiveData().observe(this@LatestMateActivity) { it ->
                L.i(":::it " + it)
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        viewModel.feachLatestMate()
                    }
                    is UiState.Failed -> {

                    }
                }

            }
        }


    }
}