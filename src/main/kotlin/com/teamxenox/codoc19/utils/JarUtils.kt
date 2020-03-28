package com.teamxenox.codoc19.utils
import com.teamxenox.codoc19.Codoc19Application
import java.io.File

object JarUtils {

    fun getJarDir(): String {

        val jarDir = File(
            Codoc19Application::class.java.protectionDomain.codeSource.location
                .toURI()
        ).parent

        if (jarDir.contains("/out/production") || jarDir.contains("/build/classes/")) {
            return ""
        }

        return "$jarDir/"
    }
}