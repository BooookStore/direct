package org.direct.domain.question

import org.direct.domain.user.User

object QuestionAuthorityPolicy {

    infix fun User.allowedEdit(question: Question): Boolean = when {
        isQuestioner(question) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.allowedPublic(question: Question): Boolean = isQuestioner(question)

    infix fun User.allowDelete(question: Question): Boolean = when {
        isQuestioner(question) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.allowResolve(question: Question): Boolean = isQuestioner(question)

    private fun User.isQuestioner(question: Question) = question.questioner == id

}