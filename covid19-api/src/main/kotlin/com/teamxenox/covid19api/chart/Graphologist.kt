package com.teamxenox.covid19api.chart

import com.teamxenox.covid19api.models.jhu.JhuData
import com.teamxenox.covid19api.utils.ArrayUtils
import com.teamxenox.covid19api.utils.JarUtils
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.knowm.xchart.XYChartBuilder
import java.awt.Color
import java.awt.Font
import java.io.File
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import javax.imageio.ImageIO

class Graphologist {

    companion object {
        private const val CHART_WIDTH = 800
        private const val CHART_HEIGHT = 600

        const val CHART_DEATH = 1
        const val CHART_CASE = 2
        const val CHART_DEATH_DAILY = 3
        const val CHART_CASE_DAILY = 4

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
        private val jhuDateFormat = SimpleDateFormat("MM/dd/yy")

        private const val WATERMARK_TEXT = "Generated with @CoDoc19Bot"
        private const val WM_FONT_SIZE = 15
        private const val WM_X = CHART_WIDTH - ((WATERMARK_TEXT.length / 1.7) * WM_FONT_SIZE).toInt()
        private const val WM_Y = CHART_HEIGHT - (WM_FONT_SIZE * 0.75).toInt()
        private val WM_FONT = Font("Ubuntu", Font.BOLD, WM_FONT_SIZE)
    }


    fun prepareChart(
            chartType: Int,
            date: String,
            data: JhuData
    ): File {

        val chart = getChart(chartType, date, data)
        val fileName = "${data.countryName.replace(" ", "_")}_${date.replace("-", "_")}.png"

        // Adding watermark
        val buffImage = BitmapEncoder.getBufferedImage(chart);
        val graphics = buffImage.graphics
        graphics.apply {
            font = WM_FONT
            color = plotBgColor
            drawString(WATERMARK_TEXT, WM_X, WM_Y)
        }.dispose()

        val chartFile = File("${JarUtils.getJarDir()}charts/$fileName")
        ImageIO.write(buffImage, "png", chartFile)

        return chartFile
    }

    private fun getChart(chartType: Int, date: String, _data: JhuData): XYChart {

        val data = if (chartType == CHART_CASE_DAILY || chartType == CHART_DEATH_DAILY) {
            JhuData(
                    _data.countryName,
                    _data.firstDeath,
                    ArrayUtils.getDiffNoNegative(_data.daySeries)
            )
        } else {
            _data
        }


        val seriesTitle: String
        val chartLineColor: Color
        val yAxisTitle: String
        val chartTitle: String

        when (chartType) {
            CHART_DEATH, CHART_DEATH_DAILY -> {
                seriesTitle = CHART_DEATH_LEGEND
                chartLineColor = Color.RED
                yAxisTitle = CHART_DEATH_Y_AXIS_TITLE
                chartTitle = CHART_DEATH_TITLE
            }
            CHART_CASE, CHART_CASE_DAILY -> {
                seriesTitle = CHART_CASE_LEGEND
                chartLineColor = Color.ORANGE
                yAxisTitle = CHART_CASE_Y_AXIS_TITLE
                chartTitle = CHART_CASE_TITLE
            }

            else -> throw IllegalArgumentException("Undefined chartType `$chartType`")
        }

        val chart = XYChartBuilder()
                .width(CHART_WIDTH)
                .height(CHART_HEIGHT)
                .title("$chartTitle - ${data.countryName} (${reformatJhuDate(data.firstDeath)} - ${reformat(date)})")
                .xAxisTitle(CHART_COMMON_X_AXIS_TITLE)
                .yAxisTitle(yAxisTitle)
                .build()

        with(chart.styler) {
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

        chart.addSeries(seriesTitle, null, data.daySeries)
                .apply {
                    lineColor = chartLineColor
                    markerColor = chartLineColor
                }

        return chart

    }

    private fun reformatJhuDate(firstDeath: String): String {
        return reformat(jhuDateFormat, firstDeath)
    }

    private fun reformat(inputDate: String): String {
        return reformat(inputDateFormat, inputDate)
    }

    private fun reformat(inputDateFormat: SimpleDateFormat, inputDate: String): String {
        return outputDateFormat.format(inputDateFormat.parse(inputDate))
    }

}