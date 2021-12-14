@file:Suppress("unused")

package org.direct.domain.user

interface UserRepository {

    fun save(user: User)

    fun findById(userId: UserId): User?

    fun exist(userId: UserId): Boolean = findById(userId) != null

}