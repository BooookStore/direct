@file:Suppress("RemoveEmptyClassBody")

package org.direct.applicationservice

import org.direct.adapter.InMemoryQuestionRepository
import org.direct.adapter.InMemoryUserRepository
import org.direct.adapter.IncrementalQuestionIdentityGenerator
import org.junit.jupiter.api.BeforeEach

internal abstract class ApplicationServiceTestSupport {

    private var inMemoryQuestionRepository: InMemoryQuestionRepository? = null

    private var inMemoryUserRepository: InMemoryUserRepository? = null

    private var incrementalQuestionIdentityGenerator: IncrementalQuestionIdentityGenerator? = null

    @BeforeEach
    fun initializeDomainRegistry() {
        inMemoryQuestionRepository = InMemoryQuestionRepository()
        inMemoryUserRepository = InMemoryUserRepository()
        incrementalQuestionIdentityGenerator = IncrementalQuestionIdentityGenerator()
    }

    fun inMemoryQuestionRepository() = inMemoryQuestionRepository ?: throw NullPointerException()

    fun inMemoryUserRepository() = inMemoryUserRepository ?: throw NullPointerException()

    fun incrementalQuestionIdentityGenerator() = incrementalQuestionIdentityGenerator ?: throw NullPointerException()

}
