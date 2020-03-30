package com.teamxenox.codoc19.data.repos

import com.teamxenox.codoc19.data.entities.Chart
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ChartRepo : CrudRepository<Chart, Int> {

    @Query("SELECT c FROM charts c WHERE c.country = :country AND c.createdAt LIKE %:date% AND c.chartType = :chartType")
    fun getChartCountryDateType(
            @Param("country") country: String,
            @Param("date") date: String,
            @Param("chartType") chartType: Chart.Type
    ): Chart?
}