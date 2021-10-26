package com.cm.taxi.view.setting.latest

import android.os.Parcelable
import com.cm.taxi.base.BaseItem
import com.cm.taxi.data.remote.model.data.GetRecentlyBoardingGroup
import com.cm.taxi.data.remote.model.data.GetTodayBoardingGroupResponse
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import kotlinx.parcelize.Parcelize


internal sealed class LatestItems(val id: String) : BaseItem(id) {
    data class Header(val data: LatestHeaderUi) : LatestItems(data.boardNo.toString())
    data class Member(val data: LatestUi) : LatestItems(data.boardNo.toString())
}