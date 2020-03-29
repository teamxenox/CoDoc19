package com.teamxenox.covid19api

import com.teamxenox.covid19api.models.GlobalApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GlobalApi {
    @GET("countries/{countryName}")
    fun getCountryData(@Path("countryName") countryName: String): Call<GlobalApiResponse>
}