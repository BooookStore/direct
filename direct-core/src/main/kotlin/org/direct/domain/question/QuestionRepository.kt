package org.direct.domain.question

interface QuestionRepository {

    fun save(question: Question)

    fun findById(questionId: QuestionId): Question?

    fun exist(questionId: QuestionId): Boolean = findById(questionId) != null

}