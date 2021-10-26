package com.cm.taxi.data.repository

import com.cm.taxi.data.Resource
import com.cm.taxi.data.getResult
import com.cm.taxi.data.remote.ResponseError
import com.cm.taxi.data.remote.TaxiApiService
import com.cm.taxi.data.remote.model.data.InsertUserInfoRequest
import com.cm.taxi.data.remote.model.data.SingleResponse
import com.cm.taxi.data.remote.model.data.UserDeleteReqeust
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.remote.model.data.UserLoginRequest
import com.cm.taxi.data.remote.model.data.UserLoginResponse
import com.squareup.moshi.JsonAdapter


class UserRepositoryImpl(
    private val taxiApiService: TaxiApiService,
    private val errorAdapter: JsonAdapter<ResponseError>,
) : UserRepository {
    override suspend fun getUserIdCheck(userId: UserIdCheckReqeust): Resource<SingleResponse> {
        return taxiApiService.getUserIdCheck(userId).getResult(errorAdapter)
    }

    override suspend fun insertUserInfo(userInfo: InsertUserInfoRequest): Resource<SingleResponse> {
        return taxiApiService.insertUserInfo(userInfo).getResult(errorAdapter)
    }

    override suspend fun userLogin(userLoginInfo: UserLoginRequest): Resource<UserLoginResponse> {
        return taxiApiService.userLogin(userLoginInfo).getResult(errorAdapter)
    }

    override suspend fun deleteUserInfo(deleteInfo: UserDeleteReqeust): Resource<SingleResponse> {
        return taxiApiService.deleteUserInfo(deleteInfo).getResult(errorAdapter)
    }
}