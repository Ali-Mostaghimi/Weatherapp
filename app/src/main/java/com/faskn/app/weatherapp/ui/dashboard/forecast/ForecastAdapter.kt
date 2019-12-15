package com.faskn.app.weatherapp.ui.dashboard.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.faskn.app.weatherapp.core.BaseAdapter
import com.faskn.app.weatherapp.databinding.ItemForecastBinding
import com.faskn.app.weatherapp.domain.model.ListItem

/**
 * Created by Furkan on 2019-10-25
 */

class ForecastAdapter(private val callBack: (ListItem, View, View, View, View, View) -> Unit) : BaseAdapter<ListItem>(diffCallback) {

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        val mBinding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewModel = ForecastItemViewModel()
        mBinding.viewModel = viewModel

        mBinding.rootView.setOnClickListener {
            mBinding.viewModel?.item?.get()?.let {
                ViewCompat.setTransitionName(mBinding.cardView, "weatherItem")
                ViewCompat.setTransitionName(mBinding.imageViewForecastIcon, "weatherItemForecastIcon")
                ViewCompat.setTransitionName(mBinding.textViewDayOfWeek, "weatherItemDayOfWeek")
                ViewCompat.setTransitionName(mBinding.textViewTemp, "weatherItemTemp")
                ViewCompat.setTransitionName(mBinding.linearLayoutTempMaxMin, "weatherItemTempMaxMin")

                callBack.invoke(
                    it,
                    mBinding.cardView,
                    mBinding.imageViewForecastIcon,
                    mBinding.textViewDayOfWeek,
                    mBinding.textViewTemp,
                    mBinding.linearLayoutTempMaxMin
                )
            }
        }
        return mBinding
    }

    override fun bind(binding: ViewDataBinding, position: Int) {
        (binding as ItemForecastBinding).viewModel?.item?.set(getItem(position))
        binding.executePendingBindings()
    }
}

val diffCallback = object : DiffUtil.ItemCallback<ListItem>() {
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.dtTxt == newItem.dtTxt
}
