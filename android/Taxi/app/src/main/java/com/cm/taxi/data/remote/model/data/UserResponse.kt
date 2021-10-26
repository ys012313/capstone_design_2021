package com.cm.taxi.data.remote.model.data

data class UserIdCheckReqeust(val userId: String)

data class InsertUserInfoRequest(val userId: String, val userPwd: String, val token: String)

data class UserLoginRequest(val userId: String, val userPwd: String, val token: String)

data class UserLoginResponse(val result: String, val user_id: String?)

data class UserDeleteReqeust(val userId: String)
