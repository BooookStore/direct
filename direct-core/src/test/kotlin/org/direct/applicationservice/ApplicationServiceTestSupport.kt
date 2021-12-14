package org.direct.applicationservice

import org.direct.adapter.InMemoryQuestionRepository
import org.direct.adapter.InMemoryUserRepository
import org.direct.adapter.IncrementalQuestionIdentityGenerator
import org.direct.domain.DomainRegistry
import org.direct.domain.DomainRegistryResolver
import org.direct.domain.question.QuestionRepository
import org.direct.domain.user.UserRepository
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

        DomainRegistry.initialize(
            TestEnvironmentDomainRegistryResolver(
                inMemoryQuestionRepository(),
                inMemoryUserRepository(),
            )
        )
    }

    fun inMemoryQuestionRepository() = inMemoryQuestionRepository ?: throw NullPointerException()

    fun inMemoryUserRepository() = inMemoryUserRepository ?: throw NullPointerException()

    fun incrementalQuestionIdentityGenerator() = incrementalQuestionIdentityGenerator ?: throw NullPointerException()

}

class TestEnvironmentDomainRegistryResolver(
    private val inMemoryQuestionRepository: InMemoryQuestionRepository,
    private val inMemoryUserRepository: InMemoryUserRepository,
) : DomainRegistryResolver {

    override fun questionRepository(): QuestionRepository = inMemoryQuestionRepository

    override fun userRepository(): UserRepository = inMemoryUserRepository

}
