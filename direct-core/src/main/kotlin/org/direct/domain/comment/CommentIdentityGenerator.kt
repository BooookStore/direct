package org.direct.domain.comment

interface CommentIdentityGenerator {

    fun generateIdentity(): CommentId

}