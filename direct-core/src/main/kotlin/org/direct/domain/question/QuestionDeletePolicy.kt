package org.direct.domain.question

import org.direct.domain.user.User

object QuestionDeletePolicy {

    infix fun User.canDelete(question: Question): Boolean = when {
        isAuthorOf(question) -> true
        isAuditor() -> true
        else -> false
    }

}