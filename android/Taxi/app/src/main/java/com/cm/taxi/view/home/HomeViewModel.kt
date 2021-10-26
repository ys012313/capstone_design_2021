package com.cm.taxi.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.Prefer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {

    private val _navSearchMate = MutableLiveData<Event<Unit>>()
    val navSearchMate: LiveData<Event<Unit>> get() = _navSearchMate

    private val _navSettingSrceen = MutableLiveData<Event<Unit>>()
    val navSettingSrceen: LiveData<Event<Unit>> get() = _navSettingSrceen

    private val _navLoginSrceen = MutableLiveData<Event<Unit>>()
    val navLoginSrceen: LiveData<Event<Unit>> get() = _navLoginSrceen

    private val _navSignUpSrceen = MutableLiveData<Event<Unit>>()
    val navSignUpSrceen: LiveData<Event<Unit>> get() = _navSignUpSrceen

    private val _navTodayMateSrceen = MutableLiveData<Event<Unit>>()
    val navTodayMateSrceen: LiveData<Event<Unit>> get() = _navTodayMateSrceen

    private val _uiState = MutableStateFlow<UiState<Int>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()

    init {
        L.i(":::인잇...")
        feachTodayBoardingCnt()
    }

    fun navToMateSearch() {
        if (Prefer.get(Prefer.KEY_USER_ID, "").isEmpty()) {
            _toast.value = "로그인이 되어있지 않습니다."
            return
        }
        _navSearchMate.value = Event(Unit)
    }

    fun navToSetting() {
        _navSettingSrceen.value = Event(Unit)
    }

    fun navTodayMate() {
        _navTodayMateSrceen.value = Event(Unit)
    }

    fun navToLogin() {
        _navLoginSrceen.value = Event(Unit)
    }

    fun navToUserRegister() {
        _navSignUpSrceen.value = Event(Unit)
    }

    fun feachTodayBoardingCnt() {
        L.i("::::::::::::::::::::::시도...")
        viewModelScope.launch {
            when (val baordingCntResponse = boardRepository.getTodayBoardingGroupCnt(UserIdCheckReqeust(userId = Prefer.get(Prefer.KEY_USER_ID, "")))) {
                is Resource.Success -> {
                    L.i("::::::::::::::::::::::시도...")
                    _uiState.value = UiState.success(baordingCntResponse.data.count)
                }
                is Resource.Error -> {
                    L.i("::::::::::::::::::::::시도...")
                    _uiState.value = UiState.failed(baordingCntResponse.message.toString())
                }
            }
        }
    }

}