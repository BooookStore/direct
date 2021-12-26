@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.question

import org.direct.domain.DomainException
import org.direct.domain.answer.AnswerId
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
    resolved: QuestionResolveStatus,
) {

    companion object {

        fun newBeforePublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = BEFORE_PUBLIC,
            resolved = QuestionUnResolve(),
        )

        fun newPublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = PUBLIC,
            resolved = QuestionUnResolve(),
        )

    }

    var title: String = title
        private set

    var subject: String = subject
        private set

    var visibility: QuestionVisibility = visibility
        private set

    var resolveStatus: QuestionResolveStatus = resolved
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

    fun resolve(resolvedAnswerId: AnswerId, operateUser: User) {
        if (resolveStatus is QuestionResolved) throw DomainException("question already resolved : questionId=[${id.rawId}]")
        if (visibility != PUBLIC) throw DomainException("can't not public question to resolve : questionId=[${id.rawId}]")
        if (canResolve(operateUser).not()) throw DomainException("user not allowd resolve : userId=[${operateUser.id.rawId}] questionId=[${id.rawId}]")

        resolveStatus = QuestionResolved(resolvedAnswerId)
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

    fun canResolve(operateUser: User): Boolean = isQuestioner(operateUser)

    private fun isQuestioner(user: User) = questioner == user.id

}