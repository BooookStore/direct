@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.question

import org.direct.domain.question.QuestionVisibility.PUBLIC
import org.direct.domain.user.UserId

class Question(
    val id: QuestionId,
    title: String,
    subject: String,
    val questioner: UserId,
    visibility: QuestionVisibility,
) {

    companion object {

        fun newPublic(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            visibility = PUBLIC
        )

    }

    var title: String = title
        private set

    var subject: String = subject
        private set

    var visibility: QuestionVisibility = visibility
        private set

    fun editTitle(newTitle: String) {
        title = newTitle
    }

    fun editSubject(newSubject: String) {
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

    override fun toString(): String {
        return "Question(id=$id, questioner=$questioner, title='$title', subject='$subject', status=$visibility)"
    }

}