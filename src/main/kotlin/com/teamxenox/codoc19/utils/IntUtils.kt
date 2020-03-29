package com.teamxenox.codoc19.utils

fun Int.get(singular: String, plural: String): String {
    return if (this <= 1) {
        singular
    } else {
        plural
    }
}