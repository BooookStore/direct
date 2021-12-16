@file:Suppress("unused")

package org.direct.applicationservice

import org.direct.domain.exception.EntityNotFoundException
import org.direct.domain.question.*
import org.direct.domain.question.QuestionEditPolicy.canEdit
import org.direct.domain.user.UserId
import org.direct.domain.user.UserRepository

class QuestionApplicationService(
    private val questionIdentityGenerator: QuestionIdentityGenerator,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) {

    fun newQuestion(command: QuestionNewCommand): QuestionId {
        command.assertUserExist()

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
        command.assertUserExist()
        command.assertQuestionExist()

        val question = questionRepository.findById(QuestionId(command.id))
            ?: throw EntityNotFoundException("question not found : $command.id")
        val editUser = userRepository.findById(UserId(command.editUserId))
            ?: throw EntityNotFoundException("user not found : $command.editUserId")

        if ((editUser canEdit question).not()) throw NotAllowedEditQuestionException("user ${command.editUserId} not allowed edit ${command.id}")

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

    private fun QuestionNewCommand.assertUserExist() {
        if (userRepository.exist(UserId(questionerUserId)).not()) throw IllegalArgumentException("user not exist")
    }

    private fun QuestionEditCommand.assertUserExist() {
        if (userRepository.exist(UserId(editUserId)).not()) throw IllegalArgumentException("user not exist")
    }

    private fun QuestionEditCommand.assertQuestionExist() {
        if (questionRepository.exist(QuestionId(id)).not()) throw IllegalArgumentException("question not exist")
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
    val editUserId: String,
)
