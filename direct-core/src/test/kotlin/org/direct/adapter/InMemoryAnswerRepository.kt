@file:Suppress("unused")

package org.direct.adapter

import org.direct.domain.answer.Answer
import org.direct.domain.answer.AnswerId
import org.direct.domain.answer.AnswerRepository

class InMemoryAnswerRepository : AnswerRepository {

    private val entities: MutableMap<AnswerId, Answer> = HashMap()

    private fun Answer.copy() = Answer(id = id, replyTo = replyTo, subject = subject, answerer = answerer)

    override fun save(answer: Answer) {
        entities[answer.id] = answer
    }

    override fun findById(answerId: AnswerId): Answer? {
        return entities[answerId]?.copy()
    }

}