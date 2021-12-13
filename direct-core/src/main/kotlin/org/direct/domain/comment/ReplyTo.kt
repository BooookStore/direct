@file:Suppress("unused")

package org.direct.domain.comment

import org.direct.domain.answer.AnswerId
import org.direct.domain.question.QuestionId

sealed interface ReplyTo

class ReplyToQuestion(val questionId: QuestionId) : ReplyTo

class ReplyToAnswer(val answerId: AnswerId) : ReplyTo