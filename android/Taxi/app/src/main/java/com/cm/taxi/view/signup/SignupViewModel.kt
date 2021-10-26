package com.cm.taxi.view.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.InsertUserInfoRequest
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.UserRepository
import com.cm.taxi.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SignupViewModel(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()

    val textId = MutableLiveData<String>()
    val textPassword = MutableLiveData<String>()
    val textPasswordConfirm = MutableLiveData<String>()
    val textNickname = MutableLiveData<String>()

    fun onUserFormComplete() {
        L.i("onUserFormComplete")
        if (textId.value.isNullOrEmpty() ||
            textPassword.value.isNullOrEmpty() ||
            textPasswordConfirm.value.isNullOrEmpty() ||
            textNickname.value.isNullOrEmpty()
        ) {
            _toast.value = "모든 정보를 기입해주세요."
            return
        }

        L.i("::::textPassword " + textPassword.value)
        L.i("::::textPasswordConfirm " + textPasswordConfirm.value)
        if (textPassword.value != textPasswordConfirm.value) {
            _toast.value = "비밀번호가 일치하지 않습니다."
            return
        }
        fetchUserInsert()
    }

    private fun fetchUserInsert() {

        _uiState.value = UiState.loading(true)

        viewModelScope.launch {

            when (val response = userRepository.getUserIdCheck(UserIdCheckReqeust(textId.value!!))) {
                is Resource.Success -> {
                    val authResponse = response.data.isSuccess()

                    if (authResponse) {
                        val signUpResponse = userRepository.insertUserInfo(
                            InsertUserInfoRequest(
                                userId = textId.value!!,
                                userPwd = textPassword.value!!,
                                token = pushToken
                            )
                        )
                        if (signUpResponse is Resource.Success) {
                            val isSignUp = signUpResponse.data.isSuccess()

                            if (isSignUp) {
                                _toast.value = "회원가입에 성공하였습니다."
                                _uiState.value = UiState.Success(Unit)
                            } else {
                                _toast.value = "회원가입에 실패하였습니다."
                            }
                        }
                    } else {
                        _toast.value = "이미 가입된 아이디가 있습니다."
                    }
                }
                is Resource.Error -> {
                    _toast.value = response.message.toString()
                }
            }


        }
    }


}