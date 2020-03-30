package com.teamxenox.codoc19.data.entities

import javax.persistence.*


@Entity(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var firstName: String? = null
    var username: String? = null
    var userId: Int? = null

    @Enumerated(EnumType.STRING)
    lateinit var platform: Platform

    enum class Platform {
        TELEGRAM, FACEBOOK
    }
}