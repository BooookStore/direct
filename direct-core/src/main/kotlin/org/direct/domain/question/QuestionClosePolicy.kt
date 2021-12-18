package org.direct.domain.question

import org.direct.domain.user.User

object QuestionClosePolicy {

    infix fun User.canClose(editQuestion: Question): Boolean = when {
        isAuthorOf(editQuestion) -> true
        isAuditor() -> true
        else -> false
    }

}
