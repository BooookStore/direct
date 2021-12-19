package org.direct.domain.question

import org.assertj.core.api.Assertions.assertThat
import org.direct.domain.question.QuestionVisibility.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class QuestionVisibilityTest {

    @Test
    fun open() {
        assertThat(CLOSED.reOpen()).isEqualTo(OPENED)
            .withFailMessage("can transit closed to opened")

        assertThrows<IllegalQuestionStatusTransitionException>("can't opened to opened") { OPENED.reOpen() }
        assertThrows<IllegalQuestionStatusTransitionException>("can't deleted to opened") { DELETED.reOpen() }
    }

    @Test
    fun close() {
        assertThat(OPENED.close()).isEqualTo(CLOSED)
            .withFailMessage("can opened to closed")

        assertThrows<IllegalQuestionStatusTransitionException>("can't closed to closed") { CLOSED.close() }
        assertThrows<IllegalQuestionStatusTransitionException>("can't deleted to closed") { DELETED.close() }
    }

    @Test
    fun delete() {
        assertThat(OPENED.delete()).isEqualTo(DELETED)
            .withFailMessage("can opened to deleted")
        assertThat(CLOSED.delete()).isEqualTo(DELETED)
            .withFailMessage("can closed to deleted")

        assertThrows<IllegalQuestionStatusTransitionException>("can't deleted to deleted") { DELETED.delete() }
    }

}