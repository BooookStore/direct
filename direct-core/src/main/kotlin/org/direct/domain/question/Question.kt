@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.question

import org.direct.domain.DomainException
import org.direct.domain.question.QuestionEditPolicy.canEdit
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
        if (editUser.canEdit(this).not()) {
            throw DomainException("user not allowed edit : userId=[${editUser.id.rawId}] questionId=[${id.rawId}]")
        }

        title = newTitle
    }

    fun editSubject(newSubject: String, editUser: User) {
        if (editUser.canEdit(this).not()) {
            throw DomainException("user not allowed edit : userId=[${editUser.id.rawId}] questionId=[${id.rawId}]")
        }

        subject = newSubject
    }

    fun public() {
        visibility = visibility.public()
    }

    fun delete() {
        visibility = visibility.delete()
    }

    fun isIdentifiedBy(otherQuestion: Question): Boolean {
        return id == otherQuestion.id
    }

}