package com.cm.taxi.view.setting.latest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cm.taxi.L
import com.cm.taxi.base.BaseViewModel
import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.BlacklistUserReqeust
import com.cm.taxi.data.remote.model.data.GetRecentlyBoardingGroup
import com.cm.taxi.data.remote.model.data.GetTodayBoardingGroupResponse
import com.cm.taxi.data.remote.model.data.UserDeleteReqeust
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.repository.BoardRepository
import com.cm.taxi.data.repository.UserRepository
import com.cm.taxi.state.UiState
import com.cm.taxi.util.Event
import com.cm.taxi.util.Prefer
import com.cm.taxi.view.home.today.TodayItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LatestViewModel(
    private val boardRepository: BoardRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<LatestItems>>>(UiState.loading(false))
    val uiState = _uiState.asStateFlow()

    private val _blackListState = MutableStateFlow<UiState<Unit>>(UiState.loading(false))
    val blackListState = _blackListState.asStateFlow()

    init {
        feachLatestMate()
    }


    fun onInsertBlockListUser(item : LatestItems.Member){
        viewModelScope.launch {
            delay(100)
            when (val reponse = boardRepository.insertBlacklistUser(BlacklistUserReqeust(item.data.boardNo.toString(),item.data.userName))) {
                is Resource.Success -> {
                    if(reponse.data.isSuccess()){
                        _toast.value = "신고에 접수 되었습니다"
                        _blackListState.value = UiState.success(Unit)
                    }else{
                        _toast.value = "신고에 접수에 실패하였습니다."
                    }

                }
                is Resource.Error -> {
                    _toast.value = reponse.message.toString()
                    _blackListState.value = UiState.failed(reponse.message.toString())
                }

            }
        }
    }


    fun feachLatestMate() {
        viewModelScope.launch {
            delay(100)
            when (val reponse = boardRepository.getRecentlyBoardingGroup(UserIdCheckReqeust(Prefer.get(Prefer.KEY_USER_ID, "")))) {
                is Resource.Success -> {
                    _uiState.value = UiState.success(mapRemoteToDomain(reponse.data))
                }
                is Resource.Error -> {
                    _uiState.value = UiState.failed(reponse.message.toString())
                }

            }
        }
    }

    private suspend fun mapRemoteToDomain(remoteLatestMates: List<GetRecentlyBoardingGroup>) : List<LatestItems> {
        return withContext(Dispatchers.Default) {
            val list = arrayListOf<LatestItems>()
            remoteLatestMates.forEach {
                list.add(LatestItems.Header(mapDomainToLatestHeaderUi(it)))
                val members = it.userList.split(",")
                members.map { name ->
                    list.add(LatestItems.Member(mapDomainToLatestUi(name, it)))
                }
            }
            list
        }
    }

    private fun mapDomainToLatestUi(userName: String, domain: GetRecentlyBoardingGroup): LatestUi {
        return LatestUi(
            userName = userName,
            leaderYn = domain.leaderYn,
            boardNo = domain.boardNo,
        )
    }

    private fun mapDomainToLatestHeaderUi(domain: GetRecentlyBoardingGroup): LatestHeaderUi {
        return LatestHeaderUi(
            title = domain.title,
            boardNo = domain.boardNo,
        )
    }

}