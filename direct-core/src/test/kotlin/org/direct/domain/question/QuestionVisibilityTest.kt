package org.direct.domain.question

import org.assertj.core.api.Assertions.assertThat
import org.direct.domain.DomainException
import org.direct.domain.question.QuestionVisibility.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class QuestionVisibilityTest {

    @Test
    fun public() {
        assertThat(BEFORE_PUBLIC.public()).isEqualTo(PUBLIC)
            .withFailMessage("can transit before_public to public")

        assertThrows<DomainException>("can't public to public") { PUBLIC.public() }
        assertThrows<DomainException>("can't deleted to public") { DELETED.public() }
    }

    @Test
    fun delete() {
        assertThat(BEFORE_PUBLIC.delete()).isEqualTo(DELETED)
            .withFailMessage("can before_public to deleted")
        assertThat(PUBLIC.delete()).isEqualTo(DELETED)
            .withFailMessage("can public to deleted")

        assertThrows<DomainException>("can't deleted to deleted") { DELETED.delete() }
    }

}