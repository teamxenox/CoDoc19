package com.teamxenox.codoc19.data.repos

import com.teamxenox.codoc19.data.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepo : CrudRepository<User, Int> {
    fun findByTgUserId(tgUserId: Int): User?
}