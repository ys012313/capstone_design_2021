package com.cm.taxi.data.remote


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
import com.cm.taxi.data.remote.model.data.InsertUserInfoRequest
import com.cm.taxi.data.remote.model.data.SingleResponse
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import com.cm.taxi.data.remote.model.data.UserDeleteReqeust
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.remote.model.data.UserLoginRequest
import com.cm.taxi.data.remote.model.data.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TaxiApiService {
    @POST("user/getUserIdCheck")
    suspend fun getUserIdCheck(@Body userId: UserIdCheckReqeust): Response<SingleResponse>

    @POST("user/insertUserInfo")
    suspend fun insertUserInfo(@Body userInfo: InsertUserInfoRequest): Response<SingleResponse>

    @POST("user/userLogin")
    suspend fun userLogin(@Body userInfo: UserLoginRequest): Response<UserLoginResponse>

    @POST("user/deleteUserInfo")
    suspend fun deleteUserInfo(@Body userInfo: UserDeleteReqeust): Response<SingleResponse>

    @POST("board/getBoardList")
    suspend fun getBoardList(@Body userId: UserIdCheckReqeust): Response<List<TaxiMateResponse>>

    @POST("board/getBoardSearchList")
    suspend fun getBoardSearchList(@Body reqeust: GetBoardSearchListRequest): Response<List<TaxiMateResponse>>

    @POST("board/createGroup")
    suspend fun createGroup(@Body request: CreateGroupReqeust): Response<SingleResponse>

    @POST("board/insertBoardingUser")
    suspend fun insertBoardingUser(@Body request: InsertBoardingRequest): Response<SingleResponse>

    @POST("board/fixBoardingGroup")
    suspend fun fixBoardingGroup(@Body request: FixBoardingRequest): Response<SingleResponse>

    @POST("board/getFixBoardingGroup")
    suspend fun getFixBoardingGroup(@Body request: GetFixBoardingGroupRequest): Response<GetFixBoardingGroupResponse>

    @POST("board/finishDrive")
    suspend fun finishDrive(@Body request: FinishDriveRequest): Response<SingleResponse>

    @POST("board/getTodayBoardingGroup")
    suspend fun getTodayBoardingGroup(@Body userId: UserIdCheckReqeust): Response<List<GetTodayBoardingGroupResponse>>

    @POST("board/getTodayBoardingGroupCnt")
    suspend fun getTodayBoardingGroupCnt(@Body userId: UserIdCheckReqeust): Response<GetTodayBoardingGroupCntResponse>

    @POST("user/insertBlacklistUser")
    suspend fun insertBlacklistUser(@Body request: BlacklistUserReqeust): Response<SingleResponse>

    @POST("user/deleteBlacklistUser")
    suspend fun deleteBlacklistUser(@Body request: BlacklistUserReqeust): Response<SingleResponse>

    @POST("user/getTotalPay")
    suspend fun getTotalPay(@Body request: UserIdCheckReqeust): Response<GetTotalPay>

    @POST("board/getRecentlyBoardingGroup")
    suspend fun getRecentlyBoardingGroup(@Body userId: UserIdCheckReqeust): Response<List<GetRecentlyBoardingGroup>>


    @POST("board/escapeDrive")
    suspend fun escapeDrive(@Body request: EscapeDriveReqeust): Response<SingleResponse>

}