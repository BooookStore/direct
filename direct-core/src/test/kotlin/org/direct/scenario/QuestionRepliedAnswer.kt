package org.direct.scenario

import org.direct.DomainRegistryInitializeTest
import org.direct.domain.DomainRegistry
import org.direct.domain.answer.Answer
import org.direct.domain.comment.Comment
import org.direct.domain.comment.ReplyToQuestion
import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.user.UserId
import org.direct.adapter.InMemoryAnswerRepository
import org.direct.adapter.InMemoryCommentRepository
import org.direct.adapter.InMemoryQuestionRepository
import org.junit.jupiter.api.Test

internal class QuestionRepliedAnswer : DomainRegistryInitializeTest() {

    private val inMemoryQuestionRepository = InMemoryQuestionRepository()

    private val inMemoryAnswerRepository = InMemoryAnswerRepository()

    private val inMemoryCommentRepository = InMemoryCommentRepository()

    @Test
    fun questionRepliedAnswer() {
        // new question
        val question = Question.new(
            id = QuestionId("QUESTION1"),
            title = "How install wget command ?",
            subject = "I want to install wget command",
            questioner = UserId("USER1"),
        )

        // save new question
        inMemoryQuestionRepository.save(question)

        // new answer
        val answer = Answer(
            id = DomainRegistry.answerIdentityGenerator().generateIdentity(),
            replyTo = question.id,
            subject = "use apt install wget",
            answerer = UserId("USER2"),
        )

        // save new answer
        inMemoryAnswerRepository.save(answer)

        // new comment for question
        val comment = Comment(
            id = DomainRegistry.commentIdentityGenerator().generateIdentity(),
            replyTo = ReplyToQuestion(question.id),
            subject = "what os use ?",
            commenter = UserId("USER3"),
        )

        // save new comment
        inMemoryCommentRepository.save(comment)
    }

}

