package com.cm.taxi.data.repository

import com.cm.taxi.data.Resource
import com.cm.taxi.data.remote.model.data.InsertUserInfoRequest
import com.cm.taxi.data.remote.model.data.SingleResponse
import com.cm.taxi.data.remote.model.data.UserDeleteReqeust
import com.cm.taxi.data.remote.model.data.UserIdCheckReqeust
import com.cm.taxi.data.remote.model.data.UserLoginRequest
import com.cm.taxi.data.remote.model.data.UserLoginResponse

interface UserRepository {
    suspend fun getUserIdCheck(userId: UserIdCheckReqeust): Resource<SingleResponse>

    suspend fun insertUserInfo(userInfo: InsertUserInfoRequest): Resource<SingleResponse>

    suspend fun userLogin(userLoginInfo: UserLoginRequest): Resource<UserLoginResponse>

    suspend fun deleteUserInfo(deleteInfo: UserDeleteReqeust): Resource<SingleResponse>
}