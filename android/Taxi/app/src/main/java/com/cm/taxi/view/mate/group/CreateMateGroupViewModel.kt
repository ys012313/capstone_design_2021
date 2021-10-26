package com.cm.taxi.view.mate.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.CreateGroupReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Prefer
import com.cm.taxi.util.Prefer.KEY_USER_ID
import com.cm.taxi.util.boardingDate
import com.cm.taxi.util.latestYearMonthFormat
import com.cm.taxi.util.remainDay
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.DateTimeException
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth


internal class CreateMateGroupViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {

    val yearMonth = MutableLiveData<String>()
    val day = MutableLiveData<String>()
    val hour = MutableLiveData<String>()
    val minute = MutableLiveData<String>()
    val startPlace = MutableLiveData<String>()
    val endPlace = MutableLiveData<String>()
    val content = MutableLiveData<String>()

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()


    init {
        yearMonth.value = LocalDate.now().latestYearMonthFormat
    }


    val _startPlace = MutableLiveData<Place>()
    val _endPlace = MutableLiveData<Place>()

    fun setStartPlace(place: Place) {
        startPlace.value = place.name
        _startPlace.value = place
    }

    fun setEndPlace(place: Place) {
        endPlace.value = place.name
        _endPlace.value = place
    }

    fun onClickCreateGroup() {


        if (day.value.isNullOrEmpty() || hour.value.isNullOrEmpty() || minute.value.isNullOrEmpty()) {
            _toast.value = "탑승시간을 입력해주세요."
            return
        }
        if (content.value.isNullOrEmpty()) {
            _toast.value = "내용을 입력해주세요."
            return
        }

        if (_startPlace.value == null || _endPlace.value == null) {
            _toast.value = "목적지를 등록해주세요."
            return
        }
        try {
            val diffDay = LocalDate.now().remainDay(day.value!!)
            val isDiffDayOne = when {
                diffDay < 0 -> {
                    _toast.value = "이전 날짜로 선택할수 없습니다."
                    false
                }
                diffDay >= 2 -> {
                    _toast.value = "하루 이상 설정할수 없습니다."
                    false
                }
                else -> true
            }

            if (isDiffDayOne == false) {
                return
            }
            L.i("::::: " + LocalDate.now().boardingDate(day.value!!, hour.value!!, minute.value!!))
            feachCreateGroup()
        } catch (e: DateTimeException) {
            _toast.value = "유효한 날짜를 지켜주세요."
        }


    }


    private fun feachCreateGroup() {

        viewModelScope.launch {
            val createGroupResonse = boardRepository.createGroup(
                CreateGroupReqeust(
                    writer = Prefer.get(KEY_USER_ID, ""),
                    startArea = _startPlace.value?.name ?: "",
                    startX = _startPlace.value?.latLng?.latitude.toString(),
                    startY = _startPlace.value?.latLng?.longitude.toString(),
                    endArea = _endPlace.value?.name ?: "",
                    endX = _endPlace.value?.latLng?.latitude.toString(),
                    endY = _endPlace.value?.latLng?.longitude.toString(),
                    content = content.value ?: "",
                    boardingDate = LocalDate.now().boardingDate(day.value!!, hour.value!!, minute.value!!)
                )
            )

            when (createGroupResonse) {
                is Resource.Success -> {
                    val createResponse = createGroupResonse.data.isSuccess()

                    if (createResponse) {
                        _toast.value = "그룹 만들기에 성공하였습니다."
                        _uiState.value = UiState.Success(Unit)
                    } else {
                        _toast.value = createGroupResonse.data.result
                    }
                }
                is Resource.Error -> {
                    _toast.value = createGroupResonse.message.toString()
                }
            }

        }

    }


}