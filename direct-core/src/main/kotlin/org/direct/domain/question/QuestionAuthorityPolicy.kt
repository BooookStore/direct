package org.direct.domain.question

import org.direct.domain.user.User

object QuestionAuthorityPolicy {

    infix fun User.canEdit(question: Question): Boolean = when {
        isQuestioner(question) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.canPublic(question: Question): Boolean = isQuestioner(question)

    infix fun User.canDelete(question: Question): Boolean = when {
        isQuestioner(question) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.canResolve(question: Question): Boolean = isQuestioner(question)

    private fun User.isQuestioner(question: Question) = question.questioner == id

}