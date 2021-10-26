package com.cm.taxi.view.complete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.FinishDriveRequest
import com.cm.taxi.data.remote.model.data.GetFixBoardingGroupRequest
import com.cm.taxi.data.remote.model.data.GetFixBoardingGroupResponse
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


internal class BoardginCompleteViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {


    private val _fixBoardItem = MutableLiveData<GetFixBoardingGroupResponse>()
    val fixBoardItem: LiveData<GetFixBoardingGroupResponse> get() = _fixBoardItem

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event<GetFixBoardingGroupResponse>> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    var isUserWriter = MutableLiveData<Boolean>().apply { value = false }

    val textBoardNo = MutableLiveData<String>()
    val textTotalCost = MutableLiveData<String>()
    val textDividCost = MutableLiveData<String>()


    fun onCompleteBoarding(item: GetFixBoardingGroupResponse) {
        viewModelScope.launch { _event.emit(Event.onShowDialog(item)) }
    }

    fun onClickDenyBottomDialog() {
        viewModelScope.launch { _event.emit(Event.onHideDialog()) }
    }

    fun onClickApplyBottomDialog(pay: String?, dutchPay: String?, bankName: String?, bankNo: String?) {

        if (pay.isNullOrEmpty() ||
            dutchPay.isNullOrEmpty() ||
            bankName.isNullOrEmpty() ||
            bankNo.isNullOrEmpty()
        ) {
            _toast.value = "모든 정보를 기입해주세요."
            return
        }

        val request = FinishDriveRequest(
            boardNo = textBoardNo.value ?: "",
            pay = pay ?: "",
            dutchPay = dutchPay ?: "",
            bankName = bankName ?: "",
            bankNo = bankNo ?: "",
        )

        feachFinishDrive(request)
    }

    private fun feachFinishDrive(reqeust: FinishDriveRequest) {
        viewModelScope.launch {
            val response = boardRepository.finishDrive(request = reqeust)

            when (response) {
                is Resource.Success -> {
                    if (response.data.isSuccess()) {
                        _uiState.value = UiState.success(Unit)
                        _toast.value = "운행 종료에 성공 하였습니다."
                    } else {
                        _uiState.value = UiState.failed(response.data.result)
                        _toast.value = "운행 종료에 실패 하였습니다."
                    }
                }
                is Resource.Error -> {
                    _uiState.value = UiState.failed(response.message.toString())
                    _toast.value = response.message.toString()
                }
            }
        }

    }

    fun setFixForm(formItem: GetFixBoardingGroupResponse?) {
        formItem?.let {
            textTotalCost.value = formItem.expectPay.toString()
            textDividCost.value = formItem.expectPayOne.toString()
        }
    }

    fun feachFixBoardingGroup(boardNo: String) {
        viewModelScope.launch {
            val response = boardRepository.getFixBoardingGroup(GetFixBoardingGroupRequest(boardNo = boardNo))

            when (response) {
                is Resource.Success -> {
                    textBoardNo.value = boardNo
                    isUserWriter.value = response.data.leader == userId
                    _fixBoardItem.value = response.data!!
                }
                is Resource.Error -> {
                    _toast.value = response.message.toString()
                }
            }

        }
    }


    sealed class Event<T> {
        class onHideDialog<T>() : Event<T>()
        class onShowDialog<T>(val data: T) : Event<T>()
    }

}