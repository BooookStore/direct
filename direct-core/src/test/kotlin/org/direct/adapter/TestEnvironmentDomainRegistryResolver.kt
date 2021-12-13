package org.direct.adapter

import org.direct.domain.DomainRegistryResolver

class TestEnvironmentDomainRegistryResolver : DomainRegistryResolver {

    override fun resolveAnswerIdentityGenerator(): IncrementalAnswerIdentityGenerator =
        IncrementalAnswerIdentityGenerator()

    override fun resolveCommentIdentityGenerator(): IncrementalCommentIdentityGenerator =
        IncrementalCommentIdentityGenerator()

}

