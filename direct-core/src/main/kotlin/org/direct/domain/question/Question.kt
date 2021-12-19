@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.direct.domain.question

import org.direct.domain.user.UserId

class Question(
    val id: QuestionId,
    title: String,
    subject: String,
    val questioner: UserId,
    status: QuestionVisibility,
) {

    companion object {

        fun new(id: QuestionId, title: String, subject: String, questioner: UserId): Question = Question(
            id = id,
            title = title,
            subject = subject,
            questioner = questioner,
            status = QuestionVisibility.OPENED,
        )

    }

    var title: String = title
        private set

    var subject: String = subject
        private set

    var status: QuestionVisibility = status
        private set

    fun editTitle(newTitle: String) {
        title = newTitle
    }

    fun editSubject(newSubject: String) {
        subject = newSubject
    }

    fun reOpen() {
        status = status.reOpen()
    }

    fun close() {
        status = status.close()
    }

    fun delete() {
        status = status.delete()
    }

    fun isIdentifiedBy(otherQuestion: Question): Boolean {
        return id == otherQuestion.id
    }

    override fun toString(): String {
        return "Question(id=$id, questioner=$questioner, title='$title', subject='$subject', status=$status)"
    }

}