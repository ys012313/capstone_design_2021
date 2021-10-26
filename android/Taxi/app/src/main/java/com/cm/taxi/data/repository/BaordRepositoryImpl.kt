package com.cm.taxi.data.repository

import com.cm.taxi.L
import com.cm.taxi.data.Resource
import com.cm.taxi.data.getResult
import com.cm.taxi.data.remote.ResponseError
import com.cm.taxi.data.remote.TaxiApiService
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
import com.squareup.moshi.JsonAdapter
import retrofit2.Response


internal class BaordRepositoryImpl(
    private val taxiApiService: TaxiApiService,
    private val errorAdapter: JsonAdapter<ResponseError>,
) : BoardRepository {
    override suspend fun getBoardList(userId: UserIdCheckReqeust): Resource<List<TaxiMateResponse>> {
        return taxiApiService.getBoardList(userId).getResult(errorAdapter)
    }

    override suspend fun getBoardSearchList(reqeust: GetBoardSearchListRequest): Resource<List<TaxiMateResponse>> {
        return taxiApiService.getBoardSearchList(reqeust).getResult(errorAdapter)
    }

    override suspend fun createGroup(request: CreateGroupReqeust): Resource<SingleResponse> {
        return taxiApiService.createGroup(request).getResult(errorAdapter)
    }

    override suspend fun insertBoardingUser(request: InsertBoardingRequest): Resource<SingleResponse> {
        return taxiApiService.insertBoardingUser(request).getResult(errorAdapter)
    }

    override suspend fun fixBoardingGroup(request: FixBoardingRequest): Resource<SingleResponse> {
        return taxiApiService.fixBoardingGroup(request).getResult(errorAdapter)
    }

    override suspend fun getFixBoardingGroup(request: GetFixBoardingGroupRequest): Resource<GetFixBoardingGroupResponse> {
        return taxiApiService.getFixBoardingGroup(request).getResult(errorAdapter)
    }

    override suspend fun finishDrive(request: FinishDriveRequest): Resource<SingleResponse> {
        return taxiApiService.finishDrive(request).getResult(errorAdapter)
    }

    override suspend fun getTodayBoardingGroup(userId: UserIdCheckReqeust): Resource<List<GetTodayBoardingGroupResponse>> {
        return taxiApiService.getTodayBoardingGroup(userId).getResult(errorAdapter)
    }

    override suspend fun getTodayBoardingGroupCnt(userId: UserIdCheckReqeust): Resource<GetTodayBoardingGroupCntResponse> {
        L.i("::::::::::::::::::::::시도...")
        return taxiApiService.getTodayBoardingGroupCnt(userId).getResult(errorAdapter)
    }

    override suspend fun insertBlacklistUser(request: BlacklistUserReqeust): Resource<SingleResponse> {
        return taxiApiService.insertBlacklistUser(request).getResult(errorAdapter)
    }

    override suspend fun deleteBlacklistUser(request: BlacklistUserReqeust): Resource<SingleResponse> {
        return taxiApiService.deleteBlacklistUser(request).getResult(errorAdapter)
    }

    override suspend fun getTotalPay(request: UserIdCheckReqeust): Resource<GetTotalPay> {
        return taxiApiService.getTotalPay(request).getResult(errorAdapter)
    }

    override suspend fun getRecentlyBoardingGroup(request: UserIdCheckReqeust): Resource<List<GetRecentlyBoardingGroup>> {
        return taxiApiService.getRecentlyBoardingGroup(request).getResult(errorAdapter)
    }

    override suspend fun escapeDrive(request: EscapeDriveReqeust): Resource<SingleResponse> {
        return taxiApiService.escapeDrive(request).getResult(errorAdapter)
    }

}