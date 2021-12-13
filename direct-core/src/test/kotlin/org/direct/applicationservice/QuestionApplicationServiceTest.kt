package org.direct.applicationservice

import org.assertj.core.api.Assertions.assertThat
import org.direct.domain.question.QuestionStatus.CLOSED
import org.direct.domain.user.UserId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class QuestionApplicationServiceTest : ApplicationServiceTestSupport() {

    private lateinit var questionApplicationService: QuestionApplicationService

    @BeforeEach
    fun beforeEach() {
        questionApplicationService = QuestionApplicationService(
            incrementalQuestionIdentityGenerator(),
            inMemoryQuestionRepository(),
        )
    }

    @Test
    fun `user can create new question`() {
        // setup
        val command = QuestionNewCommand(
            title = "how install Apache Maven ?",
            subject = "I want to install Apache Maven.",
            questionerUserId = "USER1",
        )

        // execute
        val newQuestionId = questionApplicationService.newQuestion(command)

        // verify
        inMemoryQuestionRepository().entities[newQuestionId].let {
            assertThat(it).isNotNull
            assertThat(it?.title).isEqualTo("how install Apache Maven ?")
            assertThat(it?.subject).isEqualTo("I want to install Apache Maven.")
            assertThat(it?.questioner).isEqualTo(UserId("USER1"))
        }
    }

    @Test
    fun `user can close question`() {
        // setup
        // create new question
        val newQuestionId = questionApplicationService.newQuestion(
            QuestionNewCommand(
                title = "how install Apache Maven ?",
                subject = "I want to install Apache Maven.",
                questionerUserId = "USER1",
            )
        )

        // execute
        questionApplicationService.closeQuestion(newQuestionId.rawId)

        // verify
        inMemoryQuestionRepository().entities[newQuestionId].let {
            assertThat(it?.status).isEqualTo(CLOSED)
        }
    }

}