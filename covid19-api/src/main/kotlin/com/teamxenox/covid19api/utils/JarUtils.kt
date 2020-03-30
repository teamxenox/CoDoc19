package com.teamxenox.covid19api.utils

import com.teamxenox.covid19api.CovidStatsAPI
import java.io.File

object JarUtils {

    fun getJarDir(): String {

        val jarDir = File(
                CovidStatsAPI::class.java.protectionDomain.codeSource.location
                        .toURI()
        ).parent

        if (jarDir.contains("/out/production") || jarDir.contains("/build/classes/")) {
            return ""
        }

        return "$jarDir/"
    }
}