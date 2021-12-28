@file:Suppress("unused")

package org.direct.applicationservice

import org.direct.domain.DomainException
import org.direct.domain.EntityNotFoundException
import org.direct.domain.answer.AnswerId
import org.direct.domain.answer.AnswerRepository
import org.direct.domain.question.Question
import org.direct.domain.question.QuestionId
import org.direct.domain.question.QuestionIdentityGenerator
import org.direct.domain.question.QuestionRepository
import org.direct.domain.user.User
import org.direct.domain.user.UserCategory
import org.direct.domain.user.UserId

class QuestionApplicationService(
    private val questionIdentityGenerator: QuestionIdentityGenerator,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository,
) {

    data class QuestionNewBeforePublicCommand(
        val title: String,
        val subject: String,
        val questionerUserId: String,
    )

    fun newBeforePublicQuestion(command: QuestionNewBeforePublicCommand): String {
        return domain {
            val newQuestionId = questionIdentityGenerator.generateIdentity()
            val newQuestion = Question.newBeforePublic(
                id = newQuestionId,
                title = command.title,
                subject = command.subject,
                questioner = UserId(command.questionerUserId),
            )
            questionRepository.save(newQuestion)
            newQuestionId.rawId
        }
    }

    data class QuestionNewPublicCommand(
        val title: String,
        val subject: String,
        val questionerUserId: String,
    )

    fun newPublicQuestion(command: QuestionNewPublicCommand): String {
        return domain {
            val newQuestionId = questionIdentityGenerator.generateIdentity()
            val newQuestion = Question.newPublic(
                id = newQuestionId,
                title = command.title,
                subject = command.subject,
                questioner = UserId(command.questionerUserId),
            )
            questionRepository.save(newQuestion)
            newQuestionId.rawId
        }
    }

    data class UserCommand(
        val userId: String,
        val userCategory: String,
    ) {

        fun buildDomainEntity(): User {
            try {
                return User(UserId(userId), UserCategory.valueOf(userCategory))
            } catch (exception: IllegalArgumentException) {
                throw IllegalCommandException(exception)
            }
        }

    }

    data class QuestionEditCommand(
        val questionId: String,
        val title: String,
        val subject: String,
        val editUser: UserCommand,
    )

    fun editQuestion(command: QuestionEditCommand) {
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))
        val editUser = command.editUser.buildDomainEntity()

        domain {
            question.editTitle(command.title, editUser)
            question.editSubject(command.subject, editUser)
            questionRepository.save(question)
        }
    }

    data class QuestionPublicCommand(
        val questionId: String,
        val operateUser: UserCommand,
    )

    fun publicQuestion(command: QuestionPublicCommand) {
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))
        val operateUser = command.operateUser.buildDomainEntity()

        domain {
            question.public(operateUser)
            questionRepository.save(question)
        }
    }

    data class QuestionDeleteCommand(
        val questionId: String,
        val operateUser: UserCommand,
    )

    fun deleteQuestion(command: QuestionDeleteCommand) {
        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))
        val operateUser = command.operateUser.buildDomainEntity()

        domain {
            question.delete(operateUser)
            questionRepository.save(question)
        }
    }

    data class QuestionResolveCommand(
        val questionId: String,
        val answerId: String,
        val operateUser: UserCommand,
    )

    fun resolveQuestion(command: QuestionResolveCommand) {
        if (answerRepository.exist(AnswerId(command.answerId)).not())
            throw IllegalCommandException(EntityNotFoundException("answer not found : ${command.answerId}"))

        val question = questionRepository.findByIdOrThrow(QuestionId(command.questionId))
        val operateUser = command.operateUser.buildDomainEntity()

        domain {
            question.resolve(AnswerId(command.answerId), operateUser)
            questionRepository.save(question)
        }
    }

    private fun QuestionRepository.findByIdOrThrow(questionId: QuestionId) = findById(questionId)
        ?: throw IllegalCommandException(EntityNotFoundException("question not found : ${questionId.rawId}"))

    private fun <R> domain(runDomain: () -> R): R {
        try {
            return runDomain()
        } catch (domainException: DomainException) {
            throw IllegalCommandException(domainException)
        }
    }

}
