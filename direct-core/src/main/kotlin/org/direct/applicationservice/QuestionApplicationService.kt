@file:Suppress("unused")

package org.direct.applicationservice

import org.direct.domain.exception.EntityNotFoundException
import org.direct.domain.question.*
import org.direct.domain.question.QuestionEditPolicy.canEdit
import org.direct.domain.question.QuestionPublicPolicy.canPublic
import org.direct.domain.question.QuestionDeletePolicy.canDelete
import org.direct.domain.user.UserId
import org.direct.domain.user.UserRepository

class QuestionApplicationService(
    private val questionIdentityGenerator: QuestionIdentityGenerator,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) {

    fun newQuestion(command: QuestionNewCommand): QuestionId {
        if (userRepository.exist(UserId(command.questionerUserId)).not())
            throw IllegalCommandException(EntityNotFoundException("user not found : ${command.questionerUserId}"))

        val newQuestionId = questionIdentityGenerator.generateIdentity()
        val newQuestion = Question.newPublic(
            id = newQuestionId,
            title = command.title,
            subject = command.subject,
            questioner = UserId(command.questionerUserId),
        )
        questionRepository.save(newQuestion)
        return newQuestionId
    }

    fun editQuestion(command: QuestionEditCommand) {
        val question = questionRepository.findById(QuestionId(command.questionId))
            ?: throw IllegalCommandException(EntityNotFoundException("question not found : ${command.questionId}"))
        val editUser = userRepository.findById(UserId(command.editUserId))
            ?: throw IllegalCommandException(EntityNotFoundException("user not found : ${command.editUserId}"))

        if ((editUser canEdit question).not())
            throw IllegalCommandException(NotAllowedEditQuestionException("user ${command.editUserId} not allowed edit ${command.questionId}"))

        question.editTitle(command.title)
        question.editSubject(command.subject)
        questionRepository.save(question)
    }

    fun publicQuestion(command: QuestionPublicCommand) {
        val operateUser = userRepository.findById(UserId(command.operateUserId))
            ?: throw IllegalCommandException(EntityNotFoundException("user not found : ${command.operateUserId}"))
        val question = questionRepository.findById(QuestionId(command.questionId))
            ?: throw IllegalCommandException(EntityNotFoundException("question not found : ${command.questionId}"))

        if ((operateUser canPublic question).not())
            throw IllegalCommandException(NotAllowedPublicQuestionException("user ${command.operateUserId} not allowed public ${command.questionId}"))

        question.public()
        questionRepository.save(question)
    }

    fun deleteQuestion(command: QuestionDeleteCommand) {
        val operateUser = userRepository.findByIdOrThrow(UserId(command.operateUserId))
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))

        if ((operateUser canDelete question).not())
            throw IllegalCommandException(NotAllowedDeleteQuestionException("user ${command.operateUserId} not allowd delete ${command.questionId}"))

        question.delete()
        questionRepository.save(question)
    }

    private fun UserRepository.findByIdOrThrow(userId: UserId) = findById(userId)
        ?: throw IllegalCommandException(EntityNotFoundException("user not found : ${userId.rawId}"))

    private fun QuestionRepository.findByIdOrThrow(questionId: QuestionId) = findById(questionId)
        ?: throw IllegalCommandException(EntityNotFoundException("question not found : ${questionId.rawId}"))

}

data class QuestionNewCommand(
    val title: String,
    val subject: String,
    val questionerUserId: String,
)

data class QuestionEditCommand(
    val questionId: String,
    val title: String,
    val subject: String,
    val editUserId: String,
)

data class QuestionPublicCommand(
    val questionId: String,
    val operateUserId: String,
)

data class QuestionDeleteCommand(
    val questionId: String,
    val operateUserId: String,
)