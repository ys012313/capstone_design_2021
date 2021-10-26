package com.cm.taxi.view.setting.statistics

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.asLiveData
import com.cm.taxi.L
import com.cm.taxi.R
import com.cm.taxi.base.BaseActivity
import com.cm.taxi.data.remote.model.data.GetTotalPay
import com.cm.taxi.databinding.ActivityStatisticsBinding
import com.cm.taxi.state.UiState
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class StatisticsActivity : BaseActivity<ActivityStatisticsBinding, StatisticsViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_statistics
    }

    override fun createViewModel(): StatisticsViewModel {
        val viewModel: StatisticsViewModel by viewModel()
        return viewModel
    }

    override fun loadState(bundle: Bundle) {
        binding.viewModel = viewModel
        initObserve()
    }

    override fun saveState(bundle: Bundle) {

    }

    private fun initObserve() {

        with(viewModel) {
            uiState.asLiveData().observe(this@StatisticsActivity) {
                when (it) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        L.i(":::::it " + it.data)
                        renderView(it.data)
                    }
                    is UiState.Failed -> {

                    }
                }
            }


        }
    }
    
    private fun renderView(data: GetTotalPay) {
        val chartMonthlyDataSet = arrayListOf<ChartDataModel>()
        val chartAvgDataSet = arrayListOf<ChartDataModel>()
        chartMonthlyDataSet.add(ChartDataModel(data.pay.toString(),data.pay.toFloat()))
        chartMonthlyDataSet.add(ChartDataModel(data.dutchPay.toString(),data.dutchPay.toFloat()))

        chartAvgDataSet.add(ChartDataModel(data.avgPay.toString(),data.avgPay.toFloat()))
        chartAvgDataSet.add(ChartDataModel(data.avgDutchPay.toString(),data.avgDutchPay.toFloat()))

        barChartSetting(data)


        val monthyBarChartEntryList = chartMonthlyDataSet.map {
            val index = chartMonthlyDataSet.indexOf(it).toFloat()
            val percent = it.amount
            L.i(":::index " + index + " percent " + percent)
            BarEntry(index, percent)
        }

        val avgBarChartEntryList = chartAvgDataSet.map {
            val index = chartAvgDataSet.indexOf(it).toFloat()
            val percent = it.amount
            L.i(":::index " + index + " percent " + percent)
            BarEntry(index, percent)
        }

        val barMonthlySet = BarDataSet(monthyBarChartEntryList, "")
        barMonthlySet.color = Color.rgb(158, 195, 222)

        val barAvgSet = BarDataSet(avgBarChartEntryList, "")
        barAvgSet.color = Color.rgb(158, 195, 222)


        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barMonthlySet)
        val barData = BarData(barMonthlySet).apply {
            setValueTextSize(15f)
            setDrawValues(true)
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$value" + "원"
                }
            })
        }

        val dataSets2 = ArrayList<IBarDataSet>()
        dataSets2.add(barAvgSet)
        val barData2 = BarData(barAvgSet).apply {
            setValueTextSize(15f)
            setDrawValues(true)
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "$value" + "원"
                }
            })
        }
        binding.monthlyChart.data = barData
        barData.barWidth = 0.7f
        binding.monthlyChart.notifyDataSetChanged()
        binding.monthlyChart.invalidate()

        binding.avgChart.data = barData2
        barData2.barWidth = 0.7f
        binding.avgChart.notifyDataSetChanged()
        binding.avgChart.invalidate()
    }

    private fun barChartSetting(data: GetTotalPay){
        binding.monthlyChart.run{
            setDrawBarShadow(true)//그림자
            setTouchEnabled(true)//차트 터치 막기
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 4
                setDrawLabels(true)
                textSize = 15f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toInt()) {
                            1 -> "동승 했을시"
                            0 -> "동승 안했을 시"
                            else -> throw IndexOutOfBoundsException("index out")
                        }
                    }
                }
            }

            val list = setOf(data.pay,data.dutchPay)
            val max = list.maxOrNull()?.toFloat()!! * 1.5f
            L.i(":::max " + max)
            axisLeft.run { //왼쪽 Y축
                isEnabled = false
                axisMinimum = 0f
                axisMaximum = max
                yOffset = -5f
            }

            axisRight.run {
                isEnabled = false
            }

            legend.run {
                isEnabled = false
            }

            description.run {
                isEnabled = false
            }
            animateY(1200)
        }


        binding.avgChart.run{
            setDrawBarShadow(true)//그림자
            setTouchEnabled(true)//차트 터치 막기
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                labelCount = 4
                setDrawLabels(true)
                textSize = 15f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toInt()) {
                            1 -> "동승 했을시"
                            0 -> "동승 안했을 시"
                            else -> throw IndexOutOfBoundsException("index out")
                        }
                    }
                }
            }

            val list = setOf(data.avgPay,data.avgDutchPay)
            val max = list.maxOrNull()?.toFloat()!! * 1.5f

            L.i(":::max " + max)
            axisLeft.run { //왼쪽 Y축
                isEnabled = false
                axisMinimum = 0f
                axisMaximum = max
                yOffset = -5f
            }

            axisRight.run {
                isEnabled = false
            }

            legend.run {
                isEnabled = false
            }

            description.run {
                isEnabled = false
            }
            animateY(1200)
        }
    }
}