package com.teamxenox.codoc19.data.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

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

    var tgFirstName: String? = null
    var tgUsername: String? = null
    var tgUserId: Int? = null
}