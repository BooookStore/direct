package org.direct.domain

import org.direct.domain.answer.AnswerIdentityGenerator
import org.direct.domain.comment.CommentIdentityGenerator

interface DomainRegistryResolver {

    fun resolveAnswerIdentityGenerator(): AnswerIdentityGenerator

    fun resolveCommentIdentityGenerator(): CommentIdentityGenerator

}
