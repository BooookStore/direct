package org.direct.domain.question

import org.direct.domain.user.User

object QuestionEditPolicy {

    infix fun User.canEdit(editQuestion: Question): Boolean = when {
        isAuthorOf(editQuestion) -> true
        isAuditor() -> true
        else -> false
    }

}
