package com.teamxenox.covid19api.apis

import com.teamxenox.covid19api.models.IndianApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface IndianApi {
    @GET("data.json")
    fun getData(): Call<IndianApiResponse>
}