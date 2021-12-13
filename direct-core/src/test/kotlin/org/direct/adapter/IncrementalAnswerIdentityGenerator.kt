package org.direct.adapter

import org.direct.domain.answer.AnswerId
import org.direct.domain.answer.AnswerIdentityGenerator

class IncrementalAnswerIdentityGenerator : AnswerIdentityGenerator {

    private var currentId = 0

    override fun generateIdentity(): AnswerId = AnswerId("ANSWER$currentId++")

}