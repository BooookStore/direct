package org.direct.domain

import org.direct.domain.answer.AnswerIdentityGenerator
import org.direct.domain.comment.CommentIdentityGenerator

object DomainRegistry {

    private var resolver: DomainRegistryResolver? = null

    fun initialize(initResolver: DomainRegistryResolver) {
        resolver = initResolver
    }

    fun answerIdentityGenerator(): AnswerIdentityGenerator = resolver?.resolveAnswerIdentityGenerator()
        ?: throw DomainRegistryNotInitializedException()

    fun commentIdentityGenerator(): CommentIdentityGenerator = resolver?.resolveCommentIdentityGenerator()
        ?: throw DomainRegistryNotInitializedException()

}

class DomainRegistryNotInitializedException : Exception()