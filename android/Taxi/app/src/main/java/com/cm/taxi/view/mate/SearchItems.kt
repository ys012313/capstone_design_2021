package com.cm.taxi.view.mate

import android.os.Parcelable
import com.cm.taxi.base.BaseItem
import com.cm.taxi.data.remote.model.data.TaxiMateResponse
import kotlinx.parcelize.Parcelize


internal sealed class SearchItems(val id: String) : BaseItem(id) {
    class Header() : SearchItems("")
    @Parcelize
    data class MateItem(val entity: TaxiMateResponse) : SearchItems(entity.boardNo.toString()), Parcelable
}