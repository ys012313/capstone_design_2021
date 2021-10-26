package com.cm.taxi.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.UserLoginRequest
import com.cm.taxi.data.repository.UserRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class LoginViewModel(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _navSignUpSrceen = MutableLiveData<Event<Unit>>()
    val navSignUpSrceen: LiveData<Event<Unit>> get() = _navSignUpSrceen

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()


    val textId = MutableLiveData<String>()
    val textPassword = MutableLiveData<String>()

    fun onClickLogin() {
        if (textId.value.isNullOrEmpty() || textPassword.value.isNullOrEmpty()) {
            _toast.value = "아이디 및 패스워드를 입력해주세요."
            _uiState.value = UiState.failed("")
            return
        }
        feachLogin()
    }

    fun navToSignUpSrceen() {
        _navSignUpSrceen.value = Event(Unit)
    }

    private fun feachLogin() {
        viewModelScope.launch {
            _uiState.value = UiState.loading(true)

            when (val response = userRepository.userLogin(UserLoginRequest(textId.value!!, textPassword.value!!, pushToken))) {
                is Resource.Success -> {
                    val loginState = response.data

                    if (loginState.user_id != null) {
                        _toast.value = "로그인 되었습니다."
                        _uiState.value = UiState.Success(loginState.user_id)
                    } else {
                        _toast.value = loginState.result
                    }

                }
                is Resource.Error -> {
                    _toast.value = response.message.toString()
                }
            }
        }
    }

}