package com.cm.taxi.view.home.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.EscapeDriveReqeust
import com.cm.taxi.data.remote.model.data.GetTodayBoardingGroupResponse
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.Prefer
import com.cm.taxi.util.Prefer.KEY_USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import java.util.Calendar

internal class TodayMateViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {

    private val _navBoaringScreen = MutableLiveData<Event<TodayItems.StoredBoard.ClosedBoard>>()
    val navBoaringScreen: LiveData<Event<TodayItems.StoredBoard.ClosedBoard>> get() = _navBoaringScreen

    private val _currentStoreItem = MutableLiveData<List<TodayItems>>()
    val currentStoreItem: LiveData<List<TodayItems>> = _currentStoreItem

    private val _uiState = MutableStateFlow<UiState<List<TodayItems>>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()


    init {
        feachTodayTaxiMate()
    }

    fun onSelectTaxiMateItem(item: TodayItems.StoredBoard.ClosedBoard) {
        L.i(":::::onSelectTaxiMateItem")
        _navBoaringScreen.value = Event(item)
    }

    fun onDeleteTaxiMateItem(item: TodayItems.StoredBoard.OpenedBoard) {
        L.i("::::::onDeleteTaxiMateItem ")
        val time = item.boarding.boardingTime.split(":")
        val hour = time[0]
        val minute = time[1]

        val startTime: LocalTime = LocalTime.now()
        val endTime: LocalTime = LocalTime.of(hour.toInt(), minute.toInt(), startTime.second)

        val duration = Duration.between(startTime,endTime)

        L.i(":::duration " + duration.toHours())
        if(duration.toHours() < 1){
            _toast.value = "1시간 이내는 취소할수 없습니다."
            return
        }

        viewModelScope.launch {
            delay(100)
            when (val reponse = boardRepository.escapeDrive(EscapeDriveReqeust(Prefer.get(KEY_USER_ID, ""), item.boarding.boardNo))) {
                is Resource.Success -> {
                    if (reponse.data.isSuccess()) {
                        _currentStoreItem.value?.let {
                           val list = it.toMutableList()
                            list.remove(item)
                            _currentStoreItem.value = list
                            _uiState.value = UiState.success(list)
                        }
                    } else {
                        _toast.value = "신청 취소에 실패 하였습니다"
                    }
                }
                is Resource.Error -> {
                    _toast.value = reponse.message.toString()
                }
            }
        }
    }

    fun feachTodayTaxiMate() {
        viewModelScope.launch {
            delay(100)
            when (val reponse = boardRepository.getTodayBoardingGroup(UserIdCheckReqeust(Prefer.get(KEY_USER_ID, "")))) {
                is Resource.Success -> {
                    _currentStoreItem.value = mapRemoteToDomain(reponse.data)
                    _currentStoreItem.value?.let { _uiState.value = UiState.success(it) }

                }
                is Resource.Error -> {
                    _uiState.value = UiState.failed(reponse.message.toString())
                }
            }
        }
    }

    private suspend fun mapRemoteToDomain(remoteTaxiMates: List<GetTodayBoardingGroupResponse>): List<TodayItems> {
        return withContext(Dispatchers.Default) {
            remoteTaxiMates.map {
                if (it.fixYn == "Y") {
                    TodayItems.StoredBoard.ClosedBoard(it)
                } else {
                    TodayItems.StoredBoard.OpenedBoard(it)
                }
            }
        }
    }

}