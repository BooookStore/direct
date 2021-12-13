package org.direct.adapter

import org.direct.domain.comment.Comment
import org.direct.domain.comment.CommentId
import org.direct.domain.comment.CommentRepository

class InMemoryCommentRepository : CommentRepository {

    private val entities: MutableMap<CommentId, Comment> = HashMap()

    override fun save(comment: Comment) {
        entities[comment.id] = comment
    }

}