package com.teamxenox.bootzan

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass

object JacksonUtils {
    private val mapper = ObjectMapper()

    fun <T> cast(data: Any, clazz: Class<T>): T {
        return mapper.convertValue(data, clazz)
    }
}