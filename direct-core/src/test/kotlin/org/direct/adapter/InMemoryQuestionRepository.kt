package org.direct.adapter

import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionRepository

class InMemoryQuestionRepository : QuestionRepository {

    val entities: MutableMap<QuestionId, Question> = HashMap()

    override fun save(question: Question) {
        entities[question.id] = question
    }

    override fun findById(questionId: QuestionId): Question? {
        TODO("Not yet implemented")
    }

}