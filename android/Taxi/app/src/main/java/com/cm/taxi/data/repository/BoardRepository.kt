package com.cm.taxi.data.repository

import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.BlacklistUserReqeust
import com.cm.taxi.data.remote.model.data.CreateGroupReqeust
import com.cm.taxi.data.remote.model.data.EscapeDriveReqeust
import com.cm.taxi.data.remote.model.data.FinishDriveRequest
import com.cm.taxi.data.remote.model.data.FixBoardingRequest
import com.cm.taxi.data.remote.model.data.GetBoardSearchListRequest
import com.cm.taxi.data.remote.model.data.GetFixBoardingGroupRequest
import com.cm.taxi.data.remote.model.data.GetFixBoardingGroupResponse
import com.cm.taxi.data.remote.model.data.GetRecentlyBoardingGroup
import com.cm.taxi.data.remote.model.data.GetTodayBoardingGroupCntResponse
import com.cm.taxi.data.remote.model.data.GetTodayBoardingGroupResponse
import com.cm.taxi.data.remote.model.data.GetTotalPay
import com.cm.taxi.data.remote.model.data.InsertBoardingRequest
import com.cm.taxi.data.remote.model.data.SingleResponse
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import retrofit2.Response

interface BoardRepository {
    suspend fun getBoardList(userId: UserIdCheckReqeust): Resource<List<TaxiMateResponse>>
    suspend fun getBoardSearchList(reqeust: GetBoardSearchListRequest): Resource<List<TaxiMateResponse>>
    suspend fun createGroup(request: CreateGroupReqeust): Resource<SingleResponse>

    suspend fun insertBoardingUser(request: InsertBoardingRequest): Resource<SingleResponse>
    suspend fun fixBoardingGroup(request: FixBoardingRequest): Resource<SingleResponse>
    suspend fun getFixBoardingGroup(request: GetFixBoardingGroupRequest): Resource<GetFixBoardingGroupResponse>
    suspend fun finishDrive(request: FinishDriveRequest): Resource<SingleResponse>
    suspend fun getTodayBoardingGroup(userId: UserIdCheckReqeust): Resource<List<GetTodayBoardingGroupResponse>>
    suspend fun getTodayBoardingGroupCnt(userId: UserIdCheckReqeust): Resource<GetTodayBoardingGroupCntResponse>
    suspend fun insertBlacklistUser(request: BlacklistUserReqeust): Resource<SingleResponse>
    suspend fun deleteBlacklistUser(request: BlacklistUserReqeust): Resource<SingleResponse>
    suspend fun getTotalPay(request: UserIdCheckReqeust): Resource<GetTotalPay>
    suspend fun getRecentlyBoardingGroup(request: UserIdCheckReqeust): Resource<List<GetRecentlyBoardingGroup>>
    suspend fun escapeDrive(request: EscapeDriveReqeust): Resource<SingleResponse>

}