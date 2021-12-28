package org.direct.domain.question

import org.direct.domain.DomainException
import org.direct.domain.answer.AnswerId

sealed interface QuestionResolveStatus {

    fun toResolved(answerId: AnswerId): QuestionResolved

}

class QuestionUnResolve : QuestionResolveStatus {

    override fun toResolved(answerId: AnswerId): QuestionResolved = QuestionResolved(answerId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}

class QuestionResolved(val resolvedAnswerId: AnswerId) : QuestionResolveStatus {

    override fun toResolved(answerId: AnswerId): QuestionResolved {
        throw DomainException("question already resolved")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as QuestionResolved
        if (resolvedAnswerId != other.resolvedAnswerId) return false
        return true
    }

    override fun hashCode(): Int {
        return resolvedAnswerId.hashCode()
    }

}
