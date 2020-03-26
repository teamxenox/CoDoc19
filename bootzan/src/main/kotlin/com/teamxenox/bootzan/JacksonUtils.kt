package com.teamxenox.bootzan

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass

object JacksonUtils {
    private val mapper = ObjectMapper()

    fun <T> cast(data: Any, clazz: Class<T>): T {
        println(mapper.writeValueAsString(data))
        return mapper.convertValue(data, clazz)
    }
}