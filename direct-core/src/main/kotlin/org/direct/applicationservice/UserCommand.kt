package org.direct.applicationservice

import org.direct.domain.user.User
import org.direct.domain.user.UserCategory
import org.direct.domain.user.UserId

data class UserCommand(
    val userId: String,
    val userCategory: String,
) {

    fun buildDomainEntity(): User {
        try {
            return User(UserId(userId), UserCategory.valueOf(userCategory))
        } catch (exception: IllegalArgumentException) {
            throw IllegalCommandException(exception)
        }
    }

}