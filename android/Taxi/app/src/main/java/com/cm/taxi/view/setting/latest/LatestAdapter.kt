package com.cm.taxi.view.setting.latest


import com.cm.taxi.R
import com.cm.taxi.base.BaseListAdapter
import com.cm.taxi.base.BaseViewModel
import java.lang.IllegalArgumentException

internal class LatestAdapter constructor(viewModel: BaseViewModel) : BaseListAdapter<LatestItems>(viewModel) {
    companion object {
        const val TYPE_HEADER = 100
        const val TYPE_NOMAL = 101
    }

    override fun layoutIdByViewType(viewType: Int): Int {
        return when (viewType) {
            TYPE_HEADER -> R.layout.latest_item_header_row
            TYPE_NOMAL -> R.layout.latest_item_row
            else -> throw IllegalArgumentException("invalid viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LatestItems.Header -> {
                TYPE_HEADER
            }
            is LatestItems.Member -> {
                TYPE_NOMAL
            }
        }
    }
}