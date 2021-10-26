package com.cm.taxi.view.home.today

import android.os.Parcelable
import com.cm.taxi.base.BaseItem
import com.cm.taxi.data.remote.model.data.GetTodayBoardingGroupResponse
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import kotlinx.parcelize.Parcelize


internal sealed class TodayItems(val id: String) : BaseItem(id) {
    sealed class StoredBoard(open val boarding: GetTodayBoardingGroupResponse) : TodayItems(boarding.boardNo.toString()) {
        data class ClosedBoard(override val boarding: GetTodayBoardingGroupResponse) : StoredBoard(boarding)
        data class OpenedBoard(override val boarding: GetTodayBoardingGroupResponse) : StoredBoard(boarding)
    }
}