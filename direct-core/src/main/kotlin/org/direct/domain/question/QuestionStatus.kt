package org.direct.domain.question

enum class QuestionStatus {
    OPENED,
    CLOSED,
    DELETED;

    fun reOpen(): QuestionStatus = when (this) {
        CLOSED -> OPENED
        OPENED, DELETED -> throw IllegalQuestionStatusTransitionException()
    }

    fun close(): QuestionStatus = when (this) {
        OPENED -> CLOSED
        CLOSED, DELETED -> throw IllegalQuestionStatusTransitionException()
    }

    fun delete(): QuestionStatus = when (this) {
        OPENED, CLOSED -> DELETED
        DELETED -> throw IllegalQuestionStatusTransitionException()
    }

}

class IllegalQuestionStatusTransitionException : Exception()