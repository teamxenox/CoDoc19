package com.teamxenox.bootzan.models.base

import com.google.gson.annotations.SerializedName


open class BaseResponse<T>(
        @SerializedName("data")
        val `data`: T?,
        @SerializedName("error")
        val error: Boolean, // false
        @SerializedName("error_code")
        val errorCode: Int, // 123
        @SerializedName("message")
        val message: String // Generated
)