package com.cm.taxi.view.mate.group

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.databinding.ActivityCreateMateGroupBinding
import com.cm.taxi.state.UiState
import com.cm.taxi.util.KeyboardManager
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class CreateMateGroupActivity : BaseActivity<ActivityCreateMateGroupBinding, CreateMateGroupViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_create_mate_group
    }

    override fun createViewModel(): CreateMateGroupViewModel {
        val viewMateGroupViewModel: CreateMateGroupViewModel by viewModel()
        return viewMateGroupViewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initObserve()
        initTargetLocationPlace()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initTargetLocationPlace() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val placeIntent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this)

        binding.inputStart.setOnClickListener {
            KeyboardManager.run { hideKeyboard() }
            TedRxOnActivityResult.with(this@CreateMateGroupActivity)
                .startActivityForResult(placeIntent)
                .subscribe { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        viewModel.setStartPlace(Autocomplete.getPlaceFromIntent(result.data))

                    }
                }
        }
        binding.inputEnd.setOnClickListener {
            KeyboardManager.run { hideKeyboard() }
            TedRxOnActivityResult.with(this@CreateMateGroupActivity)
                .startActivityForResult(placeIntent)
                .subscribe { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        viewModel.setEndPlace(Autocomplete.getPlaceFromIntent(result.data))

                    }
                }
        }

    }

    private fun initObserve() {

        with(viewModel) {
            uiState.asLiveData().observe(this@CreateMateGroupActivity) { it ->
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        setResult(RESULT_OK)
                        finish()
                    }
                    is UiState.Failed -> {

                    }
                }

            }
        }


    }
}