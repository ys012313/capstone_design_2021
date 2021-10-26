package com.cm.taxi.view.mate.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivitySearchBinding
import com.cm.taxi.databinding.ActivitySearchMateBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.refresh
import com.cm.taxi.util.setOnShortBlockClick
import com.cm.taxi.view.mate.SearchAdapter
import com.cm.taxi.view.mate.group.CreateMateGroupActivity
import com.cm.taxi.view.mate.join.JoinMateGroupActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
internal class ScanMateActivity : BaseActivity<ActivitySearchBinding, ScanMateViewModel>() {

    private val taxiMateAdapter by lazy { ScanAdapter(viewModel) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun createViewModel(): ScanMateViewModel {
        val viewMateViewModel: ScanMateViewModel by viewModel()
        return viewMateViewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        renderView()
        initObserve()

    }

    override fun saveState(bundle: Bundle) {

    }

    private fun renderView() {
        binding.list.run {
            layoutManager = LinearLayoutManager(context)
            adapter = taxiMateAdapter
        }

        binding.back.setOnShortBlockClick {
            finish()
        }
    }


    private fun initObserve() {

        with(viewModel) {
            navJoinGroupScreen.observe(this@ScanMateActivity, EventObserver {

                TedRxOnActivityResult.with(this@ScanMateActivity)
                    .startActivityForResult(
                        Intent(this@ScanMateActivity, JoinMateGroupActivity::class.java).apply {
                            putExtra("EXTRA_MATE", it)
                        })
                    .subscribe { result ->
                        if (result.resultCode == RESULT_OK) {
                            finish()
                        }
                    }
            })

            uiState.asLiveData().observe(this@ScanMateActivity) { it ->
                L.i(":::it " + it)
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        taxiMateAdapter.submitList(it.data)
                    }
                    is UiState.Failed -> {

                    }
                }

            }

        }


    }
}