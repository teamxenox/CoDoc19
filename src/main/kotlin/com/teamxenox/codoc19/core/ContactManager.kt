package com.teamxenox.codoc19.core

import com.google.gson.reflect.TypeToken
import com.teamxenox.bootzan.GsonUtils
import com.teamxenox.codoc19.models.Contact

object ContactManager {
    private const val CONTACT_FILE_NAME = "/india_helpline.json"
    private val contactFileJson = ContactManager::class.java.getResourceAsStream(CONTACT_FILE_NAME).bufferedReader().readText()
    private val type = object : TypeToken<List<Contact>>() {}.type
    val contacts: List<Contact> = GsonUtils.gson.fromJson(contactFileJson, type)
    val WHO_HELPLINE = Contact("01166564800", "WHO GLOBAL")

    fun getStates(): List<String> {
        return contacts.map { it.state }
    }
}