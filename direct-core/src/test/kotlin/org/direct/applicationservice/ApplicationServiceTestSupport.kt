package org.direct.applicationservice

import org.direct.adapter.InMemoryQuestionRepository
import org.direct.adapter.TestEnvironmentDomainRegistryResolver
import org.direct.domain.DomainRegistry
import org.junit.jupiter.api.BeforeEach

internal abstract class ApplicationServiceTestSupport {

    private var inMemoryQuestionRepository: InMemoryQuestionRepository? = null

    @BeforeEach
    fun initializeDomainRegistry() {
        DomainRegistry.initialize(TestEnvironmentDomainRegistryResolver())
        inMemoryQuestionRepository = InMemoryQuestionRepository()
    }

    fun inMemoryQuestionRepository() = inMemoryQuestionRepository ?: throw NullPointerException()

}