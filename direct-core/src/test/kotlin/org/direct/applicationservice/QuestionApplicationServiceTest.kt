@file:Suppress("ClassName")

package org.direct.applicationservice

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.direct.applicationservice.QuestionApplicationService.*
import org.direct.domain.DomainException
import org.direct.domain.EntityNotFoundException
import org.direct.domain.answer.Answer
import org.direct.domain.answer.AnswerId
import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionResolved
import org.direct.domain.question.QuestionUnResolve
import org.direct.domain.question.QuestionVisibility.*
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
            inMemoryAnswerRepository(),
        )
    }

    @Nested
    inner class CreateQuestionTest {

        @Test
        fun `user can create new public question`() {
            // setup
            val command = QuestionNewPublicCommand(
                title = "how install Apache Maven ?",
                subject = "I want to install Apache Maven.",
                questionerUserId = "USER1",
            )

            // execute
            val newQuestionId = questionApplicationService.newPublicQuestion(command)

            // verify
            inMemoryQuestionRepository().findById(QuestionId(newQuestionId)).let {
                assertThat(it).isNotNull
                assertThat(it?.title).isEqualTo("how install Apache Maven ?")
                assertThat(it?.subject).isEqualTo("I want to install Apache Maven.")
                assertThat(it?.questioner).isEqualTo(UserId("USER1"))
                assertThat(it?.visibility).isEqualTo(PUBLIC)
            }
        }

        @Test
        fun `user can create new before public question`() {
            // setup
            val command = QuestionNewBeforePublicCommand(
                title = "how install Apache Maven ?",
                subject = "I want to install Apache Maven.",
                questionerUserId = "USER1",
            )

            // execute
            val newQuestionId = questionApplicationService.newBeforePublicQuestion(command)

            // verify
            inMemoryQuestionRepository().findById(QuestionId(newQuestionId)).let {
                assertThat(it).isNotNull
                assertThat(it?.title).isEqualTo("how install Apache Maven ?")
                assertThat(it?.subject).isEqualTo("I want to install Apache Maven.")
                assertThat(it?.questioner).isEqualTo(UserId("USER1"))
                assertThat(it?.visibility).isEqualTo(BEFORE_PUBLIC)
            }
        }

    }

    @Nested
    inner class `already exist question which public and unresolved` {

        @BeforeEach
        fun beforeEach() {
            inMemoryQuestionRepository().save(
                Question(
                    id = QuestionId("QUESTION1"),
                    title = "how install Apache Maven ?",
                    subject = "I want to install Apache Maven.",
                    questioner = UserId("USER1"),
                    visibility = PUBLIC,
                    resolveStatus = QuestionUnResolve()
                )
            )
        }

        @Test
        fun `can edit question by questioner`() {
            // setup
            val command = QuestionEditCommand(
                questionId = "QUESTION1",
                title = "how install Apache Maven 3",
                subject = "I want to install Apache Maven.",
                editUser = UserCommand(
                    userId = "USER1",
                    userCategory = "NORMAL",
                )
            )

            // execute
            questionApplicationService.editQuestion(command)

            // verify
            inMemoryQuestionRepository().findById(QuestionId("QUESTION1")).let {
                assertThat(it).isNotNull
                assertThat(it?.title).isEqualTo("how install Apache Maven 3")
                assertThat(it?.subject).isEqualTo("I want to install Apache Maven.")
                assertThat(it?.questioner).isEqualTo(UserId("USER1"))
                assertThat(it?.visibility).isEqualTo(PUBLIC)
            }
        }

        @Test
        fun `cannot edit question by other user`() {
            // setup
            val command = QuestionEditCommand(
                questionId = "QUESTION1",
                title = "how install Apache Maven 3",
                subject = "I want to install Apache Maven.",
                editUser = UserCommand(
                    userId = "USER2",
                    userCategory = "NORMAL",
                )
            )

            // execute & verify
            assertThatThrownBy { questionApplicationService.editQuestion(command) }
                .isExactlyInstanceOf(IllegalCommandException::class.java)
                .hasCauseInstanceOf(DomainException::class.java)
        }

        @Test
        fun `can delete question by questioner`() {
            // setup
            val command = QuestionDeleteCommand(
                questionId = "QUESTION1",
                operateUser = UserCommand(
                    userId = "USER1",
                    userCategory = "NORMAL",
                )
            )

            // execute
            questionApplicationService.deleteQuestion(command)

            // verify
            inMemoryQuestionRepository().findById(QuestionId("QUESTION1")).let {
                assertThat(it).isNotNull
                assertThat(it?.visibility).isEqualTo(DELETED)
            }
        }

        @Test
        fun `cannot delete question by other user`() {
            // setup
            val command = QuestionDeleteCommand(
                questionId = "QUESTION1",
                operateUser = UserCommand(
                    userId = "USER2",
                    userCategory = "NORMAL",
                )
            )

            // execute & verify
            assertThatThrownBy { questionApplicationService.deleteQuestion(command) }
                .isExactlyInstanceOf(IllegalCommandException::class.java)
                .hasCauseInstanceOf(DomainException::class.java)
        }

    }

    @Nested
    inner class `already exist question which before public and unresolved` {

        @BeforeEach
        fun beforeEach() {
            inMemoryQuestionRepository().save(
                Question(
                    id = QuestionId("QUESTION1"),
                    title = "how install Apache Maven ?",
                    subject = "I want to install Apache Maven.",
                    questioner = UserId("USER1"),
                    visibility = BEFORE_PUBLIC,
                    resolveStatus = QuestionUnResolve(),
                )
            )
        }

        @Test
        fun `can public question by questioner`() {
            // setup
            val command = QuestionPublicCommand(
                questionId = "QUESTION1",
                operateUser = UserCommand(
                    userId = "USER1",
                    userCategory = "NORMAL",
                )
            )

            // execute
            questionApplicationService.publicQuestion(command)

            // verify
            inMemoryQuestionRepository().findById(QuestionId("QUESTION1")).let {
                assertThat(it).isNotNull
                assertThat(it?.visibility).isEqualTo(PUBLIC)
            }
        }

        @Test
        fun `cannot public question by other user`() {
            // setup
            val command = QuestionPublicCommand(
                questionId = "QUESTION1",
                operateUser = UserCommand(
                    userId = "USER2",
                    userCategory = "NORMAL",
                )
            )

            // execute & verify
            assertThatThrownBy { questionApplicationService.publicQuestion(command) }
                .isExactlyInstanceOf(IllegalCommandException::class.java)
                .hasCauseInstanceOf(DomainException::class.java)
        }

    }

    @Nested
    inner class `already exist question and answer` {

        @BeforeEach
        fun beforeEach() {
            inMemoryQuestionRepository().save(
                Question(
                    id = QuestionId("QUESTION1"),
                    title = "how install Apache Maven ?",
                    subject = "I want to install Apache Maven.",
                    questioner = UserId("USER1"),
                    visibility = PUBLIC,
                    resolveStatus = QuestionUnResolve(),
                )
            )

            inMemoryAnswerRepository().save(
                Answer(
                    id = AnswerId("ANSWER1"),
                    replyTo = QuestionId("QUESTION1"),
                    subject = "apache maven can install by ...",
                    answerer = UserId("USER2"),
                )
            )
        }

        @Test
        fun `can resolve question by questioner`() {
            // setup
            val command = QuestionResolveCommand(
                questionId = "QUESTION1",
                answerId = "ANSWER1",
                operateUser = UserCommand(
                    userId = "USER1",
                    userCategory = "NORMAL",
                ),
            )

            // execute
            questionApplicationService.resolveQuestion(command)

            // verify
            inMemoryQuestionRepository().findById(QuestionId("QUESTION1")).let {
                assertThat(it).isNotNull
                assertThat(it?.resolveStatus).isEqualTo(QuestionResolved(AnswerId("ANSWER1")))
            }
        }

        @Test
        fun `cannot resolve question by other user`() {
            // setup
            val command = QuestionResolveCommand(
                questionId = "QUESTION1",
                answerId = "ANSWER1",
                operateUser = UserCommand(
                    userId = "USER2",
                    userCategory = "NORMAL",
                ),
            )

            // execute & verify
            assertThatThrownBy { questionApplicationService.resolveQuestion(command) }
                .isExactlyInstanceOf(IllegalCommandException::class.java)
                .hasCauseInstanceOf(DomainException::class.java)
        }

        @Test
        fun `cannot resolve question by not exist answer`() {
            // setup
            val command = QuestionResolveCommand(
                questionId = "QUESTION1",
                answerId = "ANSWER2",
                operateUser = UserCommand(
                    userId = "USER2",
                    userCategory = "NORMAL",
                ),
            )

            // execute & verify
            assertThatThrownBy { questionApplicationService.resolveQuestion(command) }
                .isExactlyInstanceOf(IllegalCommandException::class.java)
                .hasCauseInstanceOf(EntityNotFoundException::class.java)
        }

    }

}