@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.question

import org.direct.domain.DomainException
import org.direct.domain.question.QuestionVisibility.BEFORE_PUBLIC
import org.direct.domain.question.QuestionVisibility.PUBLIC
import org.direct.domain.user.User
import org.direct.domain.user.UserId

class Question(
    val id: QuestionId,
    title: String,
    subject: String,
    val questioner: UserId,
    visibility: QuestionVisibility,
    resolved: Boolean,
) {

    companion object {

        fun newBeforePublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = BEFORE_PUBLIC,
            resolved = false,
        )

        fun newPublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = PUBLIC,
            resolved = false,
        )

    }

    var title: String = title
        private set

    var subject: String = subject
        private set

    var visibility: QuestionVisibility = visibility
        private set

    var resolved: Boolean = resolved
        private set

    fun editTitle(newTitle: String, editUser: User) {
        if (canEdit(editUser).not()) throw DomainException("user not allowed edit : userId=[${editUser.id.rawId}] questionId=[${id.rawId}]")
        title = newTitle
    }

    fun editSubject(newSubject: String, editUser: User) {
        if (canEdit(editUser).not()) throw DomainException("user not allowed edit : userId=[${editUser.id.rawId}] questionId=[${id.rawId}]")
        subject = newSubject
    }

    fun public(operateUser: User) {
        if (canPublic(operateUser).not()) throw DomainException("user not allowed public : userId=[${operateUser.id.rawId}] questionId=[${id.rawId}]")
        visibility = visibility.public()
    }

    fun delete(operateUser: User) {
        if (canDelete(operateUser).not()) throw DomainException("user not allowd delete : userId=[${operateUser.id.rawId}] questionId=[${id.rawId}]")
        visibility = visibility.delete()
    }

    fun isIdentifiedBy(otherQuestion: Question): Boolean {
        return id == otherQuestion.id
    }

    private fun canEdit(editUser: User): Boolean = when {
        isQuestioner(editUser) -> true
        editUser.isAuditor() -> true
        else -> false
    }

    private fun canPublic(operateUser: User): Boolean = when {
        isQuestioner(operateUser) -> true
        operateUser.isAuditor() -> true
        else -> false
    }

    fun canDelete(operateUser: User): Boolean = when {
        isQuestioner(operateUser) -> true
        operateUser.isAuditor() -> true
        else -> false
    }

    private fun isQuestioner(user: User) = questioner == user.id

}