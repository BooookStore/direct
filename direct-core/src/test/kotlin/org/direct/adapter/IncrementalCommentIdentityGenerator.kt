package org.direct.adapter

import org.direct.domain.comment.CommentId
import org.direct.domain.comment.CommentIdentityGenerator

class IncrementalCommentIdentityGenerator : CommentIdentityGenerator {

    private var currentId = 0

    override fun generateIdentity(): CommentId = CommentId("COMMENT$currentId")

}