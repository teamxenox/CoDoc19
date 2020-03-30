package com.teamxenox.codoc19.data.entities

import javax.persistence.*

/**
 * CREATE TABLE `users`(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
tg_first_name TEXT,
tg_username TEXT,
tg_user_id INT NOT NULL,
created_at TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP
);
 */
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