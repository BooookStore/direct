package org.direct.adapter

import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionRepository

class InMemoryQuestionRepository : QuestionRepository {

    private val entities: MutableMap<QuestionId, Question> = HashMap()

    private fun Question.copy() = Question(
        id = this.id,
        title = this.title,
        subject = this.subject,
        questioner = this.questioner,
        visibility = this.visibility,
        resolveStatus = this.resolveStatus,
    )

    override fun save(question: Question) {
        entities[question.id] = question.copy()
    }

    override fun findById(questionId: QuestionId): Question? {
        return entities[questionId]?.copy()
    }

}