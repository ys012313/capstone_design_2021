package com.cm.taxi.view.home.today


import com.cm.taxi.R
import com.cm.taxi.base.BaseListAdapter
import com.cm.taxi.base.BaseViewModel
import java.lang.IllegalArgumentException

internal class TodayAdapter constructor(viewModel: BaseViewModel) : BaseListAdapter<TodayItems>(viewModel) {
    companion object {
        const val TYPE_CLOSED_BOARDING = 100
        const val TYPE_OPENED_BOARDING = 101
    }

    override fun layoutIdByViewType(viewType: Int): Int {
        return when (viewType) {
            TYPE_CLOSED_BOARDING -> R.layout.today_taxi_mate_boarding_close_item_row
            TYPE_OPENED_BOARDING -> R.layout.today_taxi_mate_boarding_open_item_row
            else -> throw IllegalArgumentException("invalid viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TodayItems.StoredBoard.ClosedBoard -> {
                TYPE_CLOSED_BOARDING
            }
            is TodayItems.StoredBoard.OpenedBoard -> {
                TYPE_OPENED_BOARDING
            }
        }
    }
}