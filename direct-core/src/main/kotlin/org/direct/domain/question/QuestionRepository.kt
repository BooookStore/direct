package org.direct.domain.question

interface QuestionRepository {

    fun save(question: Question)

    fun findById(questionId: QuestionId): Question?

}