package com.cm.taxi.view.mate.join

import android.content.Intent
import android.os.Bundle
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivitySignupMateBinding
import com.cm.taxi.util.EventObserver
import com.cm.taxi.util.getParcelableExtra
import com.cm.taxi.view.complete.BoardginCompleteActivity
import com.cm.taxi.view.mate.SearchItems
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class JoinMateGroupActivity : BaseActivity<ActivitySignupMateBinding, JoinMateGroupViewModel>() {

    private val selectedMateItem: SearchItems.MateItem? by getParcelableExtra("EXTRA_MATE")

    override fun getLayoutId(): Int {
        return R.layout.activity_signup_mate
    }

    override fun createViewModel(): JoinMateGroupViewModel {
        val viewMateGroupViewModel: JoinMateGroupViewModel by viewModel()
        return viewMateGroupViewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        selectedMateItem?.let { viewModel.setCurrentItem(it) }
        initObserve()

        L.i(":::선택한.. 아이탬 $selectedMateItem")

    }

    override fun saveState(bundle: Bundle) {

    }


    private fun initObserve() {

        with(viewModel) {

            boardJoinComplete.observe(this@JoinMateGroupActivity, EventObserver {
                setResult(RESULT_OK)
                finish()
            })

            boardComplete.observe(this@JoinMateGroupActivity, EventObserver {
                TedRxOnActivityResult.with(this@JoinMateGroupActivity)
                    .startActivityForResult(
                        Intent(this@JoinMateGroupActivity, BoardginCompleteActivity::class.java).apply {
                            putExtra("EXTRA_BOARD_NO", selectedMateItem!!.entity.boardNo.toString())
                        })
                    .subscribe { _ ->
                        setResult(RESULT_OK)
                        finish()
                    }
            })
        }


    }
}