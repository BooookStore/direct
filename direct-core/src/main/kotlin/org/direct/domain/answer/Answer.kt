@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.answer

import org.direct.domain.question.QuestionId
import org.direct.domain.user.UserId

class Answer(
    val id: AnswerId,
    val replyTo: QuestionId,
    subject: String,
    val answerer: UserId,
) {

    var subject: String = subject
        private set

    fun changeSubject(newSubject: String) {
        subject = newSubject
    }

    fun isIdentifiedBy(otherAnswer: Answer): Boolean {
        return id == otherAnswer.id
    }

    override fun toString(): String {
        return "Answer(id=$id, replyTo=$replyTo, answerer=$answerer, subject='$subject')"
    }

}