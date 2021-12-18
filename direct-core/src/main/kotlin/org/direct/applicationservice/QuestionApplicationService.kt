@file:Suppress("unused")

package org.direct.applicationservice

import org.direct.domain.exception.EntityNotFoundException
import org.direct.domain.question.*
import org.direct.domain.question.QuestionClosePolicy.canClose
import org.direct.domain.question.QuestionEditPolicy.canEdit
import org.direct.domain.user.UserId
import org.direct.domain.user.UserRepository

class QuestionApplicationService(
    private val questionIdentityGenerator: QuestionIdentityGenerator,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) {

    fun newQuestion(command: QuestionNewCommand): QuestionId {
        command.questionerUserId.assertUserExist()

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
        command.questionId.assertQuestionExist()
        command.editUserId.assertUserExist()

        val question = questionRepository.findById(QuestionId(command.questionId))
            ?: throw EntityNotFoundException("question not found : $command.id")
        val editUser = userRepository.findById(UserId(command.editUserId))
            ?: throw EntityNotFoundException("user not found : $command.editUserId")

        if ((editUser canEdit question).not())
            throw IllegalCommandException(NotAllowedEditQuestionException("user ${command.editUserId} not allowed edit ${command.questionId}"))

        question.editTitle(command.title)
        question.editSubject(command.subject)
        questionRepository.save(question)
    }

    fun closeQuestion(command: QuestionCloseCommand) {
        command.questionId.assertQuestionExist()
        command.closeUserId.assertUserExist()

        val question = questionRepository.findById(QuestionId(command.questionId))
            ?: throw EntityNotFoundException("question not found : ${command.questionId}")
        val closeUser = userRepository.findById(UserId(command.closeUserId))
            ?: throw EntityNotFoundException("user not found : ${command.closeUserId}")

        if ((closeUser canClose question).not())
            throw IllegalCommandException(NotAllowedCloseQuestionException("user ${command.closeUserId} not allowed close ${command.questionId}"))

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

    private fun String.assertUserExist() {
        if (userRepository.exist(UserId(this)).not())
            throw IllegalCommandException(IllegalArgumentException("user not exist : $this"))
    }

    private fun String.assertQuestionExist() {
        if (questionRepository.exist(QuestionId(this)).not())
            throw IllegalCommandException(IllegalArgumentException("question not exist : $this"))
    }

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

data class QuestionCloseCommand(
    val questionId: String,
    val closeUserId: String,
)