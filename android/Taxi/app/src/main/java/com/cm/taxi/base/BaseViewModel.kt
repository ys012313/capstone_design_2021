package com.cm.taxi.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cm.taxi.util.Prefer


abstract class BaseViewModel : ViewModel() {

    protected val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?> = _toast

    protected val userId = Prefer.get(Prefer.KEY_USER_ID, "")
    protected val pushToken = Prefer.get(Prefer.KEY_PUSH_TOKEN, "")

    @CallSuper
    override fun onCleared() {
        super.onCleared()

    }
}