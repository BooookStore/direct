package org.direct

import org.direct.adapter.TestEnvironmentDomainRegistryResolver
import org.direct.domain.DomainRegistry
import org.junit.jupiter.api.BeforeEach

open class DomainRegistryInitializeTest {

    @BeforeEach
    fun initializeDomainRegistry() {
        DomainRegistry.initialize(TestEnvironmentDomainRegistryResolver())
    }

}