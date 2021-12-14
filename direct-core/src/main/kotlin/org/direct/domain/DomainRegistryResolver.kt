@file:Suppress("RemoveEmptyClassBody")

package org.direct.domain

import org.direct.domain.question.QuestionRepository
import org.direct.domain.user.UserRepository

interface DomainRegistryResolver {

    fun userRepository(): UserRepository

    fun questionRepository(): QuestionRepository

}
