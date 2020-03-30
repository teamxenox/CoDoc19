package com.teamxenox.covid19api.chart

import com.teamxenox.covid19api.models.jhu.JhuData
import com.teamxenox.covid19api.utils.JarUtils
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.internal.chartpart.Chart
import org.knowm.xchart.internal.series.Series
import org.knowm.xchart.style.Styler
import java.awt.Color
import java.io.File
import java.text.SimpleDateFormat

class Graphologist {

    companion object {
        private const val CHART_WIDTH = 800
        private const val CHART_HEIGHT = 600

        const val CHART_DEATH = 1
        const val CHART_CASE = 2

        private const val CHART_DEATH_TITLE = "DEATHS"
        private const val CHART_CASE_TITLE = "CONFIRMED CASES"

        private const val CHART_COMMON_X_AXIS_TITLE = "Day"
        private const val CHART_DEATH_Y_AXIS_TITLE = "Deaths"
        private const val CHART_CASE_Y_AXIS_TITLE = "People"

        private const val CHART_DEATH_LEGEND = "death"
        private const val CHART_CASE_LEGEND = "cases"

        private val chartBgColor = Color.decode("#121212")
        private val plotBgColor = Color.decode("#222222")
        private val legendBgColor = Color.decode("#272727")

        private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd")
        private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy")


    }


    fun prepareChart(
            chartType: Int,
            date: String,
            data: JhuData
    ): File {

        val chart = getChart(chartType, date, data)

        val fileName = "${data.countryName.replace(" ", "_")}_${date.replace("-", "_")}.png"
        val chartFile = File("${JarUtils.getJarDir()}charts/$fileName")
        BitmapEncoder.saveBitmap(chart, chartFile.absolutePath, BitmapEncoder.BitmapFormat.PNG);
        return chartFile
    }

    fun getChart(chartType: Int, date: String, data: JhuData): XYChart {

        val chartTitle = if (chartType == CHART_DEATH) {
            CHART_DEATH_TITLE
        } else {
            CHART_CASE_TITLE
        }

        val yAxisTitle = if (chartType == CHART_DEATH) {
            CHART_DEATH_Y_AXIS_TITLE
        } else {
            CHART_CASE_Y_AXIS_TITLE
        }

        val chartLineColor = if (chartType == CHART_DEATH) {
            Color.RED
        } else {
            Color.ORANGE
        }

        val seriesTitle = if (chartType == CHART_DEATH) {
            CHART_DEATH_LEGEND
        } else {
            CHART_CASE_LEGEND
        }

        val chart = XYChartBuilder()
                .width(CHART_WIDTH)
                .height(CHART_HEIGHT)
                .title("$chartTitle - ${data.countryName} - ${reformat(date)}")
                .xAxisTitle(CHART_COMMON_X_AXIS_TITLE)
                .theme(Styler.ChartTheme.Matlab)
                .yAxisTitle(yAxisTitle)
                .build()

        applyStyle(chart)

        chart.addSeries(seriesTitle, null, data.daySeries)
                .apply {
                    lineColor = chartLineColor
                    markerColor = chartLineColor
                }

        return chart

    }

    private fun reformat(inputDate: String): String {
        return outputDateFormat.format(inputDateFormat.parse(inputDate))
    }

    private fun applyStyle(chart: XYChart) {

        chart.styler.apply {
            isChartTitleVisible = true
            isLegendVisible = true
            xAxisLabelRotation = 45
            markerSize = 6
            chartBackgroundColor = chartBgColor
            plotBackgroundColor = plotBgColor
            axisTickLabelsColor = Color.WHITE
            chartFontColor = Color.WHITE
            legendBackgroundColor = legendBgColor
            isToolTipsAlwaysVisible = true
        }

    }
}