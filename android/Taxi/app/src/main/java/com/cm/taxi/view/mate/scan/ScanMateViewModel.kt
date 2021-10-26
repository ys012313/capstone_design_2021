package com.cm.taxi.view.mate.scan

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.GetBoardSearchListRequest
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.shareWhileObserved
import com.cm.taxi.view.mate.SearchItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@FlowPreview
internal class ScanMateViewModel(
    private val boardRepository: BoardRepository
) : BaseViewModel() {

    val focused = MutableLiveData(false)
    val query = MutableLiveData("")
    val loading = MutableLiveData(false)

    private val _uiState = MutableSharedFlow<UiState<List<SearchItems>>>()
    val uiState: SharedFlow<UiState<List<SearchItems>>> = _uiState.shareWhileObserved(viewModelScope)

    private val _navJoinGroupScreen = MutableLiveData<Event<SearchItems.MateItem>>()
    val navJoinGroupScreen: LiveData<Event<SearchItems.MateItem>> get() = _navJoinGroupScreen

    init {
        viewModelScope.launch {
            query.asFlow().distinctUntilChanged().debounce(300).filter(String::isNotEmpty).collect {
                L.i("::::::::::it " + it)
                feachTaxiMate(it)
            }
        }
    }

    fun onSelectTaxiMateItem(item: SearchItems.MateItem) {
        _navJoinGroupScreen.value = Event(item)
    }


    @WorkerThread
    fun feachTaxiMate(query : String) {
        L.e(":::feachTaxiMate>>>>>>>>>>>>>>>>>>>>>>>>")
        viewModelScope.launch {
            loading.value = true
            when (val taxiMatesResponse = boardRepository.getBoardSearchList(GetBoardSearchListRequest(query,userId))) {
                is Resource.Success -> {
                    loading.value = false
                    _uiState.emit(UiState.success(mapRemoteToDomain(taxiMatesResponse.data)))
                }
                is Resource.Error -> {
                    loading.value = false
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