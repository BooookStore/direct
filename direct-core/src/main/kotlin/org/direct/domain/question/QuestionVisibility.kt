package org.direct.domain.question

import org.direct.domain.DomainException

enum class QuestionVisibility {
    BEFORE_PUBLIC,
    PUBLIC,
    DELETED;

    fun public(): QuestionVisibility = when (this) {
        BEFORE_PUBLIC -> PUBLIC
        PUBLIC, DELETED -> throw DomainException("$this cannot transit public")
    }

    fun delete(): QuestionVisibility = when (this) {
        BEFORE_PUBLIC, PUBLIC -> DELETED
        DELETED -> throw DomainException("$this cannot transit deleted")
    }

}
