package com.cm.taxi.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


internal abstract class BaseListAdapter<T : BaseItem>(
    private val viewModel: BaseViewModel,
    diffCallback: DiffUtil.ItemCallback<T> = BaseDiffItemCallback()
) : ListAdapter<T, BaseViewHolder<T>>(diffCallback) {

    abstract fun layoutIdByViewType(viewType: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)

        val binding = createDataBinding(parent, inflater, layoutIdByViewType(viewType))

        val viewHolder = createViewHolder(binding, viewType)

        binding.setVariable(BR.viewModel, viewModel)

        onBindingAndHolderCreated(viewHolder, binding, viewType)



        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int, payloads: MutableList<Any>) {
        holder.bind(getItem(position))
    }

    open fun onBindingAndHolderCreated(viewHolder: BaseViewHolder<T>, binding: ViewDataBinding, viewType: Int) {}

    open fun createViewHolder(binding: ViewDataBinding, viewType: Int): BaseViewHolder<T> {
        return BaseViewHolder(binding)
    }

    open fun createDataBinding(parent: ViewGroup, inflater: LayoutInflater, layoutIdByViewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(inflater, layoutIdByViewType, parent, false)
    }
}
