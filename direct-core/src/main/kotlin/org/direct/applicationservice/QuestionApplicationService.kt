@file:Suppress("unused")

package org.direct.applicationservice

import org.direct.domain.DomainException
import org.direct.domain.EntityNotFoundException
import org.direct.domain.question.Question
import org.direct.domain.question.QuestionDeletePolicy.canDelete
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionIdentityGenerator
import org.direct.domain.question.QuestionPublicPolicy.canPublic
import org.direct.domain.question.QuestionRepository
import org.direct.domain.user.UserId
import org.direct.domain.user.UserRepository

class QuestionApplicationService(
    private val questionIdentityGenerator: QuestionIdentityGenerator,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) {

    data class QuestionNewBeforePublicCommand(
        val title: String,
        val subject: String,
        val questionerUserId: String,
    )

    fun newBeforePublicQuestion(command: QuestionNewBeforePublicCommand): String {
        if (userRepository.exist(UserId(command.questionerUserId)).not())
            throw IllegalCommandException(EntityNotFoundException("user not found : ${command.questionerUserId}"))

        val newQuestionId = questionIdentityGenerator.generateIdentity()
        val newQuestion = Question.newBeforePublic(
            id = newQuestionId,
            title = command.title,
            subject = command.subject,
            questioner = UserId(command.questionerUserId),
        )
        questionRepository.save(newQuestion)
        return newQuestionId.rawId
    }

    data class QuestionNewPublicCommand(
        val title: String,
        val subject: String,
        val questionerUserId: String,
    )

    fun newPublicQuestion(command: QuestionNewPublicCommand): String {
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
        return newQuestionId.rawId
    }

    data class QuestionEditCommand(
        val questionId: String,
        val title: String,
        val subject: String,
        val editUserId: String,
    )

    fun editQuestion(command: QuestionEditCommand) {
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))
        val editUser = userRepository.findByIdOrThrow(UserId(command.editUserId))

        domain {
            question.editTitle(command.title, editUser)
            question.editSubject(command.subject, editUser)
            questionRepository.save(question)
        }
    }

    data class QuestionPublicCommand(
        val questionId: String,
        val operateUserId: String,
    )

    fun publicQuestion(command: QuestionPublicCommand) {
        val operateUser = userRepository.findByIdOrThrow(UserId(command.operateUserId))
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))

        domain {
            if ((operateUser canPublic question).not()) throw DomainException("user not allowed public : userId=[${command.operateUserId}] questionId=[${command.questionId}]")

            question.public()
            questionRepository.save(question)
        }
    }

    data class QuestionDeleteCommand(
        val questionId: String,
        val operateUserId: String,
    )

    fun deleteQuestion(command: QuestionDeleteCommand) {
        val operateUser = userRepository.findByIdOrThrow(UserId(command.operateUserId))
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))

        domain {
            if ((operateUser canDelete question).not()) throw DomainException("user not allowd delete : userId=[${command.operateUserId}] questionId=[${command.questionId}]")

            question.delete()
            questionRepository.save(question)
        }
    }

    private fun UserRepository.findByIdOrThrow(userId: UserId) = findById(userId)
        ?: throw IllegalCommandException(EntityNotFoundException("user not found : ${userId.rawId}"))

    private fun QuestionRepository.findByIdOrThrow(questionId: QuestionId) = findById(questionId)
        ?: throw IllegalCommandException(EntityNotFoundException("question not found : ${questionId.rawId}"))

    private fun domain(runDomain: () -> Unit) = try {
        runDomain()
    } catch (domainException: DomainException) {
        throw IllegalCommandException(domainException)
    }

}
