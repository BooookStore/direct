package org.direct.domain.question

enum class QuestionVisibility {
    BEFORE_PUBLIC,
    PUBLIC,
    DELETED;

    fun public(): QuestionVisibility = when (this) {
        BEFORE_PUBLIC -> PUBLIC
        PUBLIC, DELETED -> throw IllegalQuestionStatusTransitionException()
    }

    fun delete(): QuestionVisibility = when (this) {
        BEFORE_PUBLIC, PUBLIC -> DELETED
        DELETED -> throw IllegalQuestionStatusTransitionException()
    }

}
