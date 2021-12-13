package org.direct.domain.question

import org.assertj.core.api.Assertions.assertThat
import org.direct.domain.question.QuestionStatus.*
import org.direct.domain.user.UserId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class QuestionTest {

    private lateinit var question: Question

    @BeforeEach
    fun beforeAll() {
        // create Question
        question = Question.new(
            id = QuestionId("123-456-789"),
            title = "TITLE",
            subject = "SUBJECT",
            questioner = UserId("123-456-789"),
        )
    }

    @Test
    fun transitStatus() {
        // verify for initial status
        assertThat(question.status).isEqualTo(OPENED)

        // execute
        question.close()

        // verify
        assertThat(question.status).isEqualTo(CLOSED)
    }

}