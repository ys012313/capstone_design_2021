package com.cm.taxi.view.mate


import com.cm.taxi.R
import com.cm.taxi.base.BaseListAdapter
import com.cm.taxi.base.BaseViewModel
import java.lang.IllegalArgumentException

internal class SearchAdapter constructor(viewModel: BaseViewModel) : BaseListAdapter<SearchItems>(viewModel) {
    companion object {
        const val TYPE_HEADER = 100
        const val TYPE_NOMAL = 101
    }

    override fun layoutIdByViewType(viewType: Int): Int {
        return when (viewType) {
            TYPE_HEADER -> R.layout.taxi_mate_item_header_row
            TYPE_NOMAL -> R.layout.taxi_mate_item_row
            else -> throw IllegalArgumentException("invalid viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchItems.Header -> {
                TYPE_HEADER
            }
            is SearchItems.MateItem -> {
                TYPE_NOMAL
            }
        }
    }
}