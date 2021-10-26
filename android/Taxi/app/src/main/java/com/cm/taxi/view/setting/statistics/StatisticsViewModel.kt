package com.cm.taxi.view.setting.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.GetTotalPay
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.shareWhileObserved
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

internal class StatisticsViewModel(
    private val boardRepository: BoardRepository,
) : BaseViewModel() {

    private val _uiState = MutableSharedFlow<UiState<GetTotalPay>>()
    val uiState: SharedFlow<UiState<GetTotalPay>> = _uiState.shareWhileObserved(viewModelScope)

    val resultTip = MutableLiveData<String>()

    init {
        featchStatistics()
    }

    private fun featchStatistics() {
        viewModelScope.launch {
            when (val response = boardRepository.getTotalPay(UserIdCheckReqeust(userId))) {
                is Resource.Success -> {
                    resultTip.value = getDiffComment(response.data)
                    _uiState.emit(UiState.success(response.data))
                }
                is Resource.Error -> {
                    _toast.value = response.message.toString()
                }
            }
        }
    }

    private fun getDiffComment(data: GetTotalPay): String {
        val monthPay = arrayListOf(data.pay, data.dutchPay)
        val avgPay = arrayListOf(data.avgPay, data.avgDutchPay)

        val monthDiff = monthPay.maxOrNull()!!.toInt() - monthPay.minOrNull()!!.toInt()
        val avgDiff = avgPay.maxOrNull()!!.toInt() - avgPay.minOrNull()!!.toInt()

        return "1회 평균 " + avgDiff + "원" + ", 월 평균 " + monthDiff + "원 을 아끼고 있어요."
    }
}