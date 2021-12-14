package org.direct.adapter

import org.direct.domain.user.User
import org.direct.domain.user.UserId
import org.direct.domain.user.UserRepository

class InMemoryUserRepository : UserRepository {

    private val entities: MutableMap<UserId, User> = HashMap()

    private fun User.copy() = User(id = this.id)

    override fun save(user: User) {
        entities[user.id] = user.copy()
    }

    override fun findById(userId: UserId): User? {
        return entities[userId]?.copy()
    }

}