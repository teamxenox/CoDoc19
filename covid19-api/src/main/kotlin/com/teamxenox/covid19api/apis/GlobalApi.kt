package com.teamxenox.covid19api.apis

import com.teamxenox.covid19api.models.GlobalAllResponse
import com.teamxenox.covid19api.models.GlobalCountryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GlobalApi {

    @GET("countries/{countryName}")
    fun getCountryData(@Path("countryName") countryName: String): Call<GlobalCountryResponse>

    @GET("all")
    fun getAll(): Call<GlobalAllResponse>

    @GET("countries")
    fun getAllCountries(): Call<List<GlobalCountryResponse>>
}