package org.direct.domain.question

import org.direct.domain.user.User

object QuestionPublicPolicy {

    infix fun User.canPublic(question: Question): Boolean = when {
        isAuthorOf(question) -> true
        isAuditor() -> true
        else -> false
    }

}