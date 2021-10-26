package com.cm.taxi.view.setting.latest

data class LatestUi(
    val userName : String,
    val leaderYn : String,
    val boardNo : Int
){
    fun isDeleteOpen() : Boolean{
        return leaderYn == "Y"
    }
}

data class LatestHeaderUi(
    val title : String,
    val boardNo : Int
)
