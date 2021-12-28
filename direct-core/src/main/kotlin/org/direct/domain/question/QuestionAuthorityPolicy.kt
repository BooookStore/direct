package org.direct.domain.question

import org.direct.domain.user.User
import org.direct.domain.user.UserId

object QuestionAuthorityPolicy {

    fun canEdit(questioner: UserId, editUser: User): Boolean = when {
        isQuestioner(questioner, editUser) -> true
        editUser.isAuditor() -> true
        else -> false
    }

    fun canPublic(questioner: UserId, operateUser: User): Boolean = when {
        isQuestioner(questioner, operateUser) -> true
        operateUser.isAuditor() -> true
        else -> false
    }

    fun canDelete(questioner: UserId, operateUser: User): Boolean = when {
        isQuestioner(questioner, operateUser) -> true
        operateUser.isAuditor() -> true
        else -> false
    }

    fun canResolve(questioner: UserId, operateUser: User): Boolean = isQuestioner(questioner, operateUser)

    private fun isQuestioner(questioner: UserId, user: User) = questioner == user.id

}