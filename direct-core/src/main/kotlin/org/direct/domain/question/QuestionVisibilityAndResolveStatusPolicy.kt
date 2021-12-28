package org.direct.domain.question

object QuestionVisibilityAndResolveStatusPolicy {

    infix fun QuestionResolveStatus.canCombine(questionVisibility: QuestionVisibility): Boolean =
        validate(questionVisibility, this)

    infix fun QuestionVisibility.canCombine(questionResolveStatus: QuestionResolveStatus): Boolean =
        validate(this, questionResolveStatus)

    fun validate(visibility: QuestionVisibility, questionResolveStatus: QuestionResolveStatus): Boolean {
        return when (visibility) {
            QuestionVisibility.BEFORE_PUBLIC -> when (questionResolveStatus) {
                is QuestionUnResolve -> true
                is QuestionResolved -> false
            }
            QuestionVisibility.PUBLIC -> when (questionResolveStatus) {
                is QuestionUnResolve -> true
                is QuestionResolved -> true
            }
            QuestionVisibility.DELETED -> when (questionResolveStatus) {
                is QuestionUnResolve -> true
                is QuestionResolved -> true
            }
        }
    }

}