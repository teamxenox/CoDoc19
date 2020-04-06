package com.teamxenox.codoc19.utils

import java.text.NumberFormat
import java.util.*

object StringUtils {

    fun addComma(number: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }

    private fun addCommaIndia(_number: Int): String {
        val number = _number.toString()
        return if (number.length >= 4) {
            when (number.length) {
                4 -> {
                    number.insert(1, ",")
                }

                5 -> {
                    number.insert(2, ",")
                }

                6 -> {
                    number.insert(1, ",")
                            .insert(4, ",")
                }

                7 -> {
                    number.insert(2, ",")
                            .insert(5, ",")
                }

                8 -> {
                    number.insert(1, ",")
                            .insert(4, ",")
                            .insert(7, ",")
                }

                9 -> {
                    number.insert(2, ",")
                            .insert(5, ",")
                            .insert(8, ",")
                }

                else -> number
            }
        } else {
            number
        }
    }
}

fun String.insert(index: Int, string: String): String {
    return this.substring(0, index) + string + this.substring(index, this.length)
}