package org.direct.adapter

import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionIdentityGenerator

class IncrementalQuestionIdentityGenerator : QuestionIdentityGenerator {

    private var currentId = 0

    override fun generateIdentity(): QuestionId = QuestionId("QUESTION${currentId++}")

}