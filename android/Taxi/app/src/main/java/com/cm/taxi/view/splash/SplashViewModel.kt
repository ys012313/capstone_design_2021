package com.cm.taxi.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.util.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SplashViewModel : BaseViewModel() {
    private val _navMainSrceen = MutableLiveData<Event<Unit>>()
    val navMainSrceen: LiveData<Event<Unit>> get() = _navMainSrceen

    fun nextScreen(){
        viewModelScope.launch {
            delay(50)
            _navMainSrceen.value = Event(Unit)
        }
    }
}