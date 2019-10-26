package com.faskn.app.weatherapp.ui.dashboard

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.faskn.app.weatherapp.R
import com.faskn.app.weatherapp.core.BaseFragment
import com.faskn.app.weatherapp.databinding.FragmentDashboardBinding
import com.faskn.app.weatherapp.domain.model.ListItem
import com.faskn.app.weatherapp.domain.usecase.CurrentWeatherUseCase
import com.faskn.app.weatherapp.domain.usecase.ForecastUseCase
import com.faskn.app.weatherapp.ui.dashboard.forecast.ForecastAdapter
import com.faskn.app.weatherapp.utils.extensions.isNetworkAvailable

class DashboardFragment : BaseFragment<DashboardFragmentViewModel, FragmentDashboardBinding>(DashboardFragmentViewModel::class.java) {

    override fun getLayoutRes() = R.layout.fragment_dashboard

    override fun initViewModel() {
        mBinding.viewModel = viewModel
    }

    override fun init() {
        super.init()

        initForecastAdapter()

        viewModel.getForecast(ForecastUseCase.ForecastParams("Istanbul,TR", isNetworkAvailable(requireContext()), "metric"))
        viewModel.getForecastLiveData().observe(
            this,
            Observer {
                with(mBinding) {
                    viewState = it
                }

                it.data?.list?.let { data -> initForecast(data) }
            }
        )

        viewModel.getCurrentWeather((CurrentWeatherUseCase.CurrentWeatherParams("Istanbul,TR", isNetworkAvailable(requireContext()), "metric")))
        viewModel.getCurrentWeatherLiveData().observe(
            this,
            Observer {
                with(mBinding) {
                    containerForecast.viewState = it
                }
            }
        )
    }

    private fun initForecastAdapter() {
        val adapter = ForecastAdapter { item ->
        }

        mBinding.recyclerForecast.adapter = adapter
        mBinding.recyclerForecast.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initForecast(list: List<ListItem>) {
        (mBinding.recyclerForecast.adapter as ForecastAdapter).submitList(
            list
                .filter { it.dtTxt?.substringAfter(" ").equals("12:00:00") }
                .distinctBy { it.dtTxt?.substringBefore(" ") }
                .drop(1)
        )
    }
}
