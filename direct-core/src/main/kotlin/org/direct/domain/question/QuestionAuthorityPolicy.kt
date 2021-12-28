package org.direct.domain.question

import org.direct.domain.user.User
import org.direct.domain.user.UserId

object QuestionAuthorityPolicy {

    infix fun User.allowedEdit(question: Question): Boolean = when {
        isQuestioner(question.questioner) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.allowedPublic(question: Question): Boolean = when {
        isQuestioner(question.questioner) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.allowDelete(question: Question): Boolean = when {
        isQuestioner(question.questioner) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.allowResolve(question: Question): Boolean = isQuestioner(question.questioner)

    private fun User.isQuestioner(questioner: UserId) = questioner == id

}