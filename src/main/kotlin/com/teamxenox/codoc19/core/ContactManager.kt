package com.teamxenox.codoc19.core

import com.google.gson.reflect.TypeToken
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.models.Contact
import com.teamxenox.codoc19.utils.JarUtils
import java.io.File

object ContactManager {
    private val contactFile = File("${JarUtils.getJarDir()}india_helpline.json")
    private val type = object : TypeToken<List<Contact>>() {}.type
    val contacts: List<Contact> = GsonUtils.gson.fromJson<List<Contact>>(contactFile.readText(), type)
    val WHO_HELPLINE = Contact("01166564800", "WHO GLOBAL")

    fun getStates(): List<String> {
        return contacts.map { it.state }
    }
}