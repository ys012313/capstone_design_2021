package com.cm.taxi.data.remote.model.data

data class SingleResponse(val result: String){
    fun isSuccess(): Boolean = result == "Success"
}
