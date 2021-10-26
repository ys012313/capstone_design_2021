package com.cm.taxi.view.mate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.shareWhileObserved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class SearchMateViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {

    private val _navCreateMateGroupScreen = MutableLiveData<Event<Unit>>()
    val navCreateMateGroupScreen: LiveData<Event<Unit>> get() = _navCreateMateGroupScreen

    private val _navSearchScreen = MutableLiveData<Event<Unit>>()
    val navSearchScreen: LiveData<Event<Unit>> get() = _navSearchScreen

    private val _navJoinGroupScreen = MutableLiveData<Event<SearchItems.MateItem>>()
    val navJoinGroupScreen: LiveData<Event<SearchItems.MateItem>> get() = _navJoinGroupScreen

    private val _uiState = MutableSharedFlow<UiState<List<SearchItems>>>()
    val uiState: SharedFlow<UiState<List<SearchItems>>> = _uiState.shareWhileObserved(viewModelScope)

    init {
        feachTaxiMate()
    }

    fun onClickNavSearchScreen() {
        _navSearchScreen.value = Event(Unit)
    }

    fun onClickNavCreateMateGroupScreen() {
        _navCreateMateGroupScreen.value = Event(Unit)
    }

    fun onSelectTaxiMateItem(item: SearchItems.MateItem) {
        _navJoinGroupScreen.value = Event(item)
    }

    fun feachTaxiMate() {
        L.e(":::feachTaxiMate>>>>>>>>>>>>>>>>>>>>>>>>")
        viewModelScope.launch {
            when (val taxiMatesResponse = boardRepository.getBoardList(UserIdCheckReqeust(userId))) {
                is Resource.Success -> {
                    _uiState.emit(UiState.success(mapRemoteToDomain(taxiMatesResponse.data)))
                }
                is Resource.Error -> {
                    _uiState.emit(UiState.failed(taxiMatesResponse.message.toString()))
                }
            }
        }
    }

    private suspend fun mapRemoteToDomain(remoteTaxiMates: List<TaxiMateResponse>): List<SearchItems> {
        return withContext(Dispatchers.Default) {
            remoteTaxiMates.map {
                SearchItems.MateItem(it)
            }
        }
    }

}