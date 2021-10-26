package com.cm.taxi.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.UserDeleteReqeust
import com.cm.taxi.data.repository.UserRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.Prefer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SettingViewModel(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _navBackPress = MutableLiveData<Event<Unit>>()
    val navBackPress: LiveData<Event<Unit>> get() = _navBackPress

    private val _navLatestMateScreen = MutableLiveData<Event<Unit>>()
    val navLatestMateScreen: LiveData<Event<Unit>> get() = _navLatestMateScreen

    private val _navUseRateScreen = MutableLiveData<Event<Unit>>()
    val navUseRateScreen: LiveData<Event<Unit>> get() = _navUseRateScreen

    private val _navUserDelete = MutableLiveData<Event<Unit>>()
    val navUserDelete: LiveData<Event<Unit>> get() = _navUserDelete

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()


    fun onClickNavBackpress() {
        _navBackPress.value = Event(Unit)
    }

    fun onClickNavLatestMateScreen() {
        _navLatestMateScreen.value = Event(Unit)
    }

    fun onClickNavUseRateScreen() {
        _navUseRateScreen.value = Event(Unit)
    }

    fun onClickDeleteUser() {
        _navUserDelete.value = Event(Unit)
    }

    fun onClickApplyBottomDialog(){
        feachDeleteUser()
    }


    private fun feachDeleteUser() {

        val userId = Prefer.get(Prefer.KEY_USER_ID, "")

        if (!userId.isNotEmpty()) {
            _toast.value = "로그인이 되어있지 않습니다."
            return
        }

        viewModelScope.launch {
            when (val response = userRepository.deleteUserInfo(UserDeleteReqeust(userId))) {
                is Resource.Success -> {
                    val loginState = response.data
                    if (loginState.isSuccess()) {
                        Prefer.set(Prefer.KEY_USER_ID, "")
                        _toast.value = "회원 탈퇴 되었습니다."
                    }
                    _uiState.value = UiState.success(Unit)
                }
                is Resource.Error -> {
                    _uiState.value = UiState.failed(response.message.toString())
                    _toast.value = response.message.toString()
                }
            }
        }
    }

}