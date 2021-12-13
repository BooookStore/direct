package org.direct.domain

object DomainRegistry {

    private var resolver: DomainRegistryResolver? = null

    fun initialize(initResolver: DomainRegistryResolver) {
        resolver = initResolver
    }

}
