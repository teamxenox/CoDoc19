package com.teamxenox.covid19api.chart

import com.teamxenox.covid19api.models.jhu.JhuData
import com.winterbe.expekt.should
import org.junit.Test
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.QuickChart
import org.knowm.xchart.SwingWrapper


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
    fun testDeathChartSuccess() {

        val deaths = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 4, 5, 4, 7, 10, 10, 12, 20, 20, 24)
        val cases = listOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 28, 30, 31, 34, 39, 43, 56, 62, 73, 82, 102, 113, 119, 142, 156, 194, 244, 330, 396, 499, 536, 657, 727, 887, 987)
        deaths.size.should.equal(cases.size)

        val chart = Graphologist().prepareChart(
                Graphologist.CHART_DEATH,
                "2020-03-31",
                JhuData("India", "2020-03-11", deaths)
        )

    }

}