@file:Suppress("unused")

package org.direct.applicationservice

import org.direct.domain.exception.EntityNotFoundException
import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionIdentityGenerator
import org.direct.domain.question.QuestionRepository
import org.direct.domain.user.UserId
import org.direct.domain.user.UserRepository

class QuestionApplicationService(
    private val questionIdentityGenerator: QuestionIdentityGenerator,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) {

    fun newQuestion(command: QuestionNewCommand): QuestionId {
        val newQuestionId = questionIdentityGenerator.generateIdentity()
        val newQuestion = Question.new(
            id = newQuestionId,
            title = command.title,
            subject = command.subject,
            questioner = UserId(command.questionerUserId),
        )
        questionRepository.save(newQuestion)
        return newQuestionId
    }

    fun editQuestion(command: QuestionEditCommand) {
        val question = questionRepository.findById(QuestionId(command.id))
            ?: throw EntityNotFoundException("question not found : $command.id")
        question.editTitle(command.title)
        question.editSubject(command.subject)
        questionRepository.save(question)
    }

    fun closeQuestion(questionId: String) {
        val question = questionRepository.findById(QuestionId(questionId))
            ?: throw EntityNotFoundException("question not found : $questionId")
        question.close()
        questionRepository.save(question)
    }

    fun reOpenQuestion(questionId: String) {
        val question = questionRepository.findById(QuestionId(questionId))
            ?: throw EntityNotFoundException("question not found : $questionId")
        question.reOpen()
        questionRepository.save(question)
    }

    fun deleteQuestion(questionId: String) {
        val question = questionRepository.findById(QuestionId(questionId))
            ?: throw EntityNotFoundException("question not found : $questionId")
        question.delete()
        questionRepository.save(question)
    }

}

data class QuestionNewCommand(
    val title: String,
    val subject: String,
    val questionerUserId: String,
)

data class QuestionEditCommand(
    val id: String,
    val title: String,
    val subject: String,
)
