package org.direct.applicationservice

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.direct.domain.question.NotAllowedEditQuestionException
import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionStatus.CLOSED
import org.direct.domain.question.QuestionStatus.OPENED
import org.direct.domain.user.User
import org.direct.domain.user.UserCategory.NORMAL
import org.direct.domain.user.UserId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class QuestionApplicationServiceTest : ApplicationServiceTestSupport() {

    private lateinit var questionApplicationService: QuestionApplicationService

    @BeforeEach
    fun beforeEach() {
        questionApplicationService = QuestionApplicationService(
            incrementalQuestionIdentityGenerator(),
            inMemoryQuestionRepository(),
            inMemoryUserRepository(),
        )

        inMemoryUserRepository().save(User(UserId("USER1"), NORMAL))
    }

    @Nested
    inner class CreateQuestionTest {

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
        fun `can not create new question without user`() {
            // setup
            val command = QuestionNewCommand(
                title = "how install Apache Maven ?",
                subject = "I want to install Apache Maven.",
                questionerUserId = "DONT EXIST USER !",
            )

            // execute & verify
            assertThatThrownBy { questionApplicationService.newQuestion(command) }
                .isExactlyInstanceOf(IllegalArgumentException::class.java)
        }

    }

    @Nested
    inner class AlreadyQuestionCreated {

        @BeforeEach
        fun beforeEach() {
            inMemoryUserRepository().save(User(UserId("USER2"), NORMAL))
            inMemoryQuestionRepository().save(
                Question(
                    id = QuestionId("QUESTION1"),
                    title = "how install Apache Maven ?",
                    subject = "I want to install Apache Maven.",
                    questioner = UserId("USER1"),
                    status = OPENED
                )
            )
        }

        @Nested
        inner class EditQuestion {

            @Test
            fun `can edit question by questioner`() {
                val command = QuestionEditCommand(
                    questionId = "QUESTION1",
                    title = "how install Apache Maven 3",
                    subject = "I want to install Apache Maven.",
                    editUserId = "USER1",
                )

                // execute
                questionApplicationService.editQuestion(command)

                // verify
                inMemoryQuestionRepository().findById(QuestionId("QUESTION1")).let {
                    assertThat(it).isNotNull
                    assertThat(it?.title).isEqualTo("how install Apache Maven 3")
                    assertThat(it?.subject).isEqualTo("I want to install Apache Maven.")
                    assertThat(it?.questioner).isEqualTo(UserId("USER1"))
                    assertThat(it?.status).isEqualTo(OPENED)
                }
            }

            @Test
            fun `cannot edit question by other user`() {
                val command = QuestionEditCommand(
                    questionId = "QUESTION1",
                    title = "how install Apache Maven 3",
                    subject = "I want to install Apache Maven.",
                    editUserId = "USER2",
                )

                // execute & verify
                assertThatThrownBy { questionApplicationService.editQuestion(command) }
                    .isExactlyInstanceOf(NotAllowedEditQuestionException::class.java)
            }

        }

    }

    @Test
    fun `user can close question`() {
        // setup
        // create new question
        inMemoryQuestionRepository().save(
            Question(
                id = QuestionId("QUESTION1"),
                title = "how install Apache Maven ?",
                subject = "I want to install Apache Maven.",
                questioner = UserId("USER1"),
                status = OPENED
            )
        )

        // execute
        questionApplicationService.closeQuestion("QUESTION1")

        // verify
        inMemoryQuestionRepository().entities[QuestionId("QUESTION1")].let {
            assertThat(it?.status).isEqualTo(CLOSED)
        }
    }

}