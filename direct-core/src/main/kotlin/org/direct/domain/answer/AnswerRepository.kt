package org.direct.domain.answer

interface AnswerRepository {

    fun save(answer: Answer)

    fun findById(answerId: AnswerId): Answer?

    fun exist(answerId: AnswerId) = findById(answerId) != null

}