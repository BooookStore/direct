package org.direct.domain.question

enum class QuestionVisibility {
    OPENED,
    CLOSED,
    DELETED;

    fun reOpen(): QuestionVisibility = when (this) {
        CLOSED -> OPENED
        OPENED, DELETED -> throw IllegalQuestionStatusTransitionException()
    }

    fun close(): QuestionVisibility = when (this) {
        OPENED -> CLOSED
        CLOSED, DELETED -> throw IllegalQuestionStatusTransitionException()
    }

    fun delete(): QuestionVisibility = when (this) {
        OPENED, CLOSED -> DELETED
        DELETED -> throw IllegalQuestionStatusTransitionException()
    }

}

class IllegalQuestionStatusTransitionException : Exception()