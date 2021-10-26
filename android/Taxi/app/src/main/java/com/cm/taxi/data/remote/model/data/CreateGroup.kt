package com.cm.taxi.data.remote.model.data

data class CreateGroupReqeust(
    val writer: String,
    val startArea: String,
    val startX: String,
    val startY: String,
    val endArea: String,
    val endX: String,
    val endY: String,
    val content: String,
    val boardingDate: String
    )