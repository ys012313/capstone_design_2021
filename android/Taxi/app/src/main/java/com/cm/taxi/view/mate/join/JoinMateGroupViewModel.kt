package com.cm.taxi.view.mate.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.FixBoardingRequest
import com.cm.taxi.data.remote.model.data.InsertBoardingRequest
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.Prefer
import com.cm.taxi.util.Prefer.KEY_USER_ID
import com.cm.taxi.util.shareWhileObserved
import com.cm.taxi.view.mate.SearchItems
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


internal class JoinMateGroupViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {

    private val _currentMateItem = MutableLiveData<SearchItems.MateItem>()
    val currentMateItem: LiveData<SearchItems.MateItem> get() = _currentMateItem

    private val _boardJoinComplete = MutableLiveData<Event<Unit>>()
    val boardJoinComplete: LiveData<Event<Unit>> get() = _boardJoinComplete

    private val _boardComplete = MutableLiveData<Event<Unit>>()
    val boardComplete: LiveData<Event<Unit>> get() = _boardComplete


    var isUserWriter = MutableLiveData<Boolean>().apply { value = false }


    fun setCurrentItem(item: SearchItems.MateItem) {
        isUserWriter.value = item.entity.writer == Prefer.get(KEY_USER_ID, "")
        _currentMateItem.value = item
    }


    fun feachFixBoardingGroup() {
        L.i(":::::::::::::::::::::::::::::feachFixBoardingGroup..")
        viewModelScope.launch {
            _currentMateItem.value ?: return@launch

            val boardingPersion = _currentMateItem.value!!.entity.boardingPersons

            if (boardingPersion < 2) {
                _toast.value = "탑승인원이 최소 2명이상 되어야 합니다."
                return@launch
            }
            val response = boardRepository.fixBoardingGroup(
                FixBoardingRequest(
                    boardNo = _currentMateItem.value!!.entity.boardNo.toString(),
                    startX = _currentMateItem.value!!.entity.startY,
                    startY = _currentMateItem.value!!.entity.startX,
                    endX = _currentMateItem.value!!.entity.endX,
                    endY = _currentMateItem.value!!.entity.endY,
                )
            )
            when (response) {
                is Resource.Success -> {
                    val createResponse = response.data.isSuccess()
                    if (createResponse) {
                        _toast.value = "탑승 완료 되었습니다."
                        _boardComplete.value = Event(Unit)
                    } else {
                        _toast.value = response.data.result
                    }
                }
                is Resource.Error -> {
                    _toast.value = response.message.toString()
                }
            }

        }
    }

    fun feachJoinGroup() {
        _currentMateItem.value?.let { item ->
            if (item.entity.boardingYn == "Y") {
                _toast.value = "이미 동승을 신청하였습니다."
                return@let
            }

            viewModelScope.launch {
                val response = boardRepository.insertBoardingUser(
                    InsertBoardingRequest(
                        boardNo = item.entity.boardNo.toString(),
                        userId = Prefer.get(KEY_USER_ID, "")
                    )
                )
                when (response) {
                    is Resource.Success -> {
                        val createResponse = response.data.isSuccess()
                        if (createResponse) {
                            _toast.value = "동승 신청에 성공하였습니다."
                            _boardJoinComplete.value = Event(Unit)
                        } else {
                            _toast.value = response.data.result
                        }
                    }
                    is Resource.Error -> {
                        _toast.value = response.message.toString()
                    }
                }

            }
        }


    }


}