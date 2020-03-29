package com.teamxenox.covid19api.chart

import com.winterbe.expekt.should
import org.junit.Test
import org.knowm.xchart.*
import org.knowm.xchart.style.Styler
import java.awt.Color


class GraphologistTest {
    @Test
    fun testDrawSuccess() {
        val xData = doubleArrayOf(0.0, 1.0, 2.0)
        val yData = doubleArrayOf(10.0, 1.0, 0.0)
        val chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData)
        SwingWrapper(chart).displayChart()
        BitmapEncoder.saveBitmap(chart, "../charts/chart.png", BitmapEncoder.BitmapFormat.PNG);
    }

    @Test
    fun testIfCanGraphologistCanDrawSuccess(){
        val graphologist = Graphologist()
    }

    @Test
    fun testDeathChartSuccess() {

        val deaths = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 4.0, 5.0, 4.0, 7.0, 10.0, 10.0, 12.0, 20.0, 20.0, 24.0)
        val cases = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 2.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 5.0, 5.0, 28.0, 30.0, 31.0, 34.0, 39.0, 43.0, 56.0, 62.0, 73.0, 82.0, 102.0, 113.0, 119.0, 142.0, 156.0, 194.0, 244.0, 330.0, 396.0, 499.0, 536.0, 657.0, 727.0, 887.0, 987.0)

        deaths.size.should.equal(cases.size)

        val chartWidth = 800
        val chartHeight = 600

        val deathChart = XYChartBuilder()
                .width(chartWidth)
                .height(chartHeight)
                .title("Deaths")
                .xAxisTitle(("Day"))
                .theme(Styler.ChartTheme.Matlab)
                .yAxisTitle("Deaths")
                .build()

        applyStyle(deathChart)

        deathChart.addSeries("deaths", null, deaths)
                .apply {
                    lineColor = Color.RED
                    markerColor = Color.RED
                }

        val casesChart = XYChartBuilder()
                .width(chartWidth)
                .height(chartHeight)
                .title("Confirmed Cases")
                .xAxisTitle(("Day"))
                .theme(Styler.ChartTheme.Matlab)
                .yAxisTitle("People")
                .build()

        applyStyle(casesChart)

        casesChart.addSeries("cases", null, cases).apply {
            lineColor = Color.ORANGE
            markerColor = Color.ORANGE
        }

        BitmapEncoder.saveBitmap(listOf(deathChart, casesChart), 1, 2, "../charts/chart.png", BitmapEncoder.BitmapFormat.PNG);
        // BitmapEncoder.saveBitmap(deathChart, "../charts/chart.png", BitmapEncoder.BitmapFormat.PNG);

    }

    private fun applyStyle(chart: XYChart) {
        chart.styler.apply {
            isChartTitleVisible = true
            isLegendVisible = true
            xAxisLabelRotation = 45
            markerSize = 6
            chartBackgroundColor = Color.decode("#121212")
            plotBackgroundColor = Color.decode("#222222")
            axisTickLabelsColor = Color.WHITE
            chartFontColor = Color.WHITE
            legendBackgroundColor = Color.decode("#272727")
        }
    }

}