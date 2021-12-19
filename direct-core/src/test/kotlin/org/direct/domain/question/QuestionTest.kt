package org.direct.domain.question

import org.assertj.core.api.Assertions.assertThat
import org.direct.domain.question.QuestionVisibility.*
import org.direct.domain.user.UserId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class QuestionTest {

    private lateinit var question: Question

    @BeforeEach
    fun beforeAll() {
        // create Question
        question = Question.newPublic(
            id = QuestionId("123-456-789"),
            title = "TITLE",
            subject = "SUBJECT",
            questioner = UserId("123-456-789"),
        )
    }

    @Test
    fun transitStatus() {
        // verify for initial status
        assertThat(question.visibility).isEqualTo(PUBLIC)

        // execute
        question.delete()

        // verify
        assertThat(question.visibility).isEqualTo(DELETED)
    }

}