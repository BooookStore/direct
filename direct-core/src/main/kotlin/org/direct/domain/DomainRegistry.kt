@file:Suppress("unused")

package org.direct.domain

import org.direct.domain.question.QuestionRepository
import org.direct.domain.user.UserRepository

object DomainRegistry {

    private var resolver: DomainRegistryResolver? = null

    fun initialize(initResolver: DomainRegistryResolver) {
        resolver = initResolver
    }

    fun userRepository(): UserRepository {
        return resolver?.userRepository() ?: throw DomainRegistryNotInitializedException()
    }

    fun questionRepository(): QuestionRepository {
        return resolver?.questionRepository() ?: throw DomainRegistryNotInitializedException()
    }

}

class DomainRegistryNotInitializedException : Throwable()
