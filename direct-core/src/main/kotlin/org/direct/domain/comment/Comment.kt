@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.comment

import org.direct.domain.user.UserId

class Comment(
    val id: CommentId,
    val replyTo: ReplyTo,
    subject: String,
    val commenter: UserId,
) {

    var subject: String = subject
        private set

    fun changeSubject(newSubject: String) {
        subject = newSubject
    }

    fun isIdentifiedBy(otherComment: Comment): Boolean {
        return id == otherComment.id
    }

}