@file:Suppress("MemberVisibilityCanBePrivate")

package org.direct.domain.user

import org.direct.domain.question.Question
import org.direct.domain.user.UserCategory.AUDITOR

class User(val id: UserId, val category: UserCategory) {

    fun isAuthorOf(question: Question): Boolean {
        return question.questioner == id
    }

    fun isAuditor(): Boolean {
        return category == AUDITOR
    }

}