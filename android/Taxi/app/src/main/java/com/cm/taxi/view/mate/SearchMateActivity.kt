package com.cm.taxi.view.mate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivitySearchMateBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.refresh
import com.cm.taxi.view.mate.group.CreateMateGroupActivity
import com.cm.taxi.view.mate.join.JoinMateGroupActivity
import com.cm.taxi.view.mate.scan.ScanMateActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
internal class SearchMateActivity : BaseActivity<ActivitySearchMateBinding, SearchMateViewModel>() {

    private val taxiMateAdapter by lazy { SearchAdapter(viewModel) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search_mate
    }

    override fun createViewModel(): SearchMateViewModel {
        val viewMateViewModel: SearchMateViewModel by viewModel()
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
            viewModel.feachTaxiMate()
        }.launchIn(lifecycleScope)
    }


    private fun initObserve() {

        with(viewModel) {
            navSearchScreen.observe(this@SearchMateActivity, EventObserver {
                startActivity(Intent(this@SearchMateActivity, ScanMateActivity::class.java))
            })

            navCreateMateGroupScreen.observe(this@SearchMateActivity, EventObserver {
                TedRxOnActivityResult.with(this@SearchMateActivity)
                    .startActivityForResult(Intent(this@SearchMateActivity, CreateMateGroupActivity::class.java))
                    .subscribe { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            viewModel.feachTaxiMate()
                        }
                    }
            })

            navJoinGroupScreen.observe(this@SearchMateActivity, EventObserver {

                TedRxOnActivityResult.with(this@SearchMateActivity)
                    .startActivityForResult(
                        Intent(this@SearchMateActivity, JoinMateGroupActivity::class.java).apply {
                        putExtra("EXTRA_MATE", it)
                    })
                    .subscribe { result ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            viewModel.feachTaxiMate()
                        }
                    }
            })

            uiState.asLiveData().observe(this@SearchMateActivity) { it ->
                L.i(":::it " + it)
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