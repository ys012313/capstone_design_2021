package com.cm.taxi.data.remote.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TaxiMateResponse(
    val writer: String,
    val boardNo: Int,
    val boardingDate: String,
    val boardingTime: String,
    val boardingDT : String,
    val startArea: String,
    val startX: String,
    val startY: String,
    val endArea: String,
    val endX: String,
    val endY: String,
    val boardingPersons: Int,
    val maxPersons: Int,
    val content: String,
    val boardingYn : String
) : Parcelable


data class EscapeDriveReqeust(val userId : String,val boardNo: Int)
data class GetBoardSearchListRequest(val endArea: String,val userId : String)

data class InsertBoardingRequest(val boardNo: String, val userId: String)

data class FixBoardingRequest(
    val boardNo: String,
    val startX: String,
    val startY: String,
    val endX: String,
    val endY: String,
)


data class GetFixBoardingGroupRequest(val boardNo: String)


@Parcelize
data class GetFixBoardingGroupResponse(
    val boardingDate: String,
    val boardingTime: String,
    val boardingDT : String,
    val startArea: String,
    val endArea: String,
    val boardingPersons: Int,
    val maxPersons: Int,
    val expectTime: String,
    val expectPay: Int,
    val expectPayOne: Int,
    val leader: String
) : Parcelable {
    fun stringExpectPayOne(): String = "$expectPayOne 원"
    fun stringExpectPay(): String = "$expectPay 원"
}


data class FinishDriveRequest(
    val boardNo: String,
    val pay: String,
    val dutchPay: String,
    val bankName: String,
    val bankNo: String,
    val pushYn: String = "Y"
)

data class GetTodayBoardingGroupResponse(
    val boardNo: Int,
    val boardingDate: String,
    val boardingTime: String,
    val boardingDT : String,
    val startArea: String,
    val endArea: String,
    val boardingPersons: Int,
    val maxPersons: Int,
    val leaderYn: String,
    val fixYn: String
)

data class GetTodayBoardingGroupCntResponse(val count: Int)

data class BlacklistUserReqeust(
    val boardNo: String,
    val userId: String,
)

data class GetTotalPay(
    val pay: Int,
    val dutchPay: Int,
    val avgPay: Int,
    val avgDutchPay: Int,

    )

data class GetRecentlyBoardingGroup(
    val boardNo: Int,
    val title: String,
    val leaderYn: String,
    val userList: String,
)