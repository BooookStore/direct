package org.direct.domain.question

import org.direct.domain.user.User
import org.direct.domain.user.UserId

object QuestionAuthorityPolicy {

    infix fun User.canEdit(question: Question): Boolean = when {
        isQuestioner(question.questioner) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.canPublic(question: Question): Boolean = when {
        isQuestioner(question.questioner) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.canDelete(question: Question): Boolean = when {
        isQuestioner(question.questioner) -> true
        isAuditor() -> true
        else -> false
    }

    infix fun User.canResolve(question: Question): Boolean = isQuestioner(question.questioner)

    private fun User.isQuestioner(questioner: UserId) = questioner == id

}