@file:Suppress("MemberVisibilityCanBePrivate")

package org.direct.domain.question

import org.direct.domain.DomainException
import org.direct.domain.answer.AnswerId
import org.direct.domain.question.QuestionAuthorityPolicy.canDelete
import org.direct.domain.question.QuestionAuthorityPolicy.canResolve
import org.direct.domain.question.QuestionAuthorityPolicy.canEdit
import org.direct.domain.question.QuestionAuthorityPolicy.canPublic
import org.direct.domain.question.QuestionVisibility.*
import org.direct.domain.user.User
import org.direct.domain.user.UserId

class Question(
    val id: QuestionId,
    title: String,
    subject: String,
    val questioner: UserId,
    visibility: QuestionVisibility,
    resolveStatus: QuestionResolveStatus,
) {

    init {
        QuestionVisibilityAndResolveStatusPolicy.validate(visibility, resolveStatus)
    }

    companion object {

        fun newBeforePublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = BEFORE_PUBLIC,
            resolveStatus = QuestionUnResolve(),
        )

        fun newPublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = PUBLIC,
            resolveStatus = QuestionUnResolve(),
        )

    }

    var title: String = title
        private set

    var subject: String = subject
        private set

    var visibility: QuestionVisibility = visibility
        private set

    var resolveStatus: QuestionResolveStatus = resolveStatus
        private set

    fun editTitle(newTitle: String, editUser: User) {
        if (visibility == DELETED) throw DomainException("question already deleted : questionId=[${id.rawId}]")
        if ((editUser canEdit this).not()) throw DomainException("user not allowed edit : userId=[${editUser.id.rawId}] questionId=[${id.rawId}]")

        title = newTitle
    }

    fun editSubject(newSubject: String, editUser: User) {
        if (visibility == DELETED) throw DomainException("question already deleted : questionId=[${id.rawId}]")
        if ((editUser canEdit this).not()) throw DomainException("user not allowed edit : userId=[${editUser.id.rawId}] questionId=[${id.rawId}]")

        subject = newSubject
    }

    fun public(operateUser: User) {
        if ((operateUser canPublic this).not()) throw DomainException("user not allowed public : userId=[${operateUser.id.rawId}] questionId=[${id.rawId}]")

        visibility = visibility.public()
    }

    fun delete(operateUser: User) {
        if ((operateUser canDelete this).not()) throw DomainException("user not allowed delete : userId=[${operateUser.id.rawId}] questionId=[${id.rawId}]")

        visibility = visibility.delete()
    }

    fun resolve(resolvedAnswerId: AnswerId, operateUser: User) {
        if ((visibility == DELETED) or (visibility == BEFORE_PUBLIC)) throw DomainException("question already deleted : questionId=[${id.rawId}]")
        if ((operateUser canResolve this).not()) throw DomainException("user not allowed resolve : userId=[${operateUser.id.rawId}] questionId=[${id.rawId}]")

        resolveStatus = resolveStatus.toResolved(resolvedAnswerId)
    }

}