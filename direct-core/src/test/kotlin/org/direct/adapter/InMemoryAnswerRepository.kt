package org.direct.adapter

import org.direct.domain.answer.Answer
import org.direct.domain.answer.AnswerId
import org.direct.domain.answer.AnswerRepository

class InMemoryAnswerRepository : AnswerRepository {

    private val entities: MutableMap<AnswerId, Answer> = HashMap()

    override fun save(answer: Answer) {
        entities[answer.id] = answer
    }

}