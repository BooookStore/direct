package org.direct.domain.question

object QuestionVisibilityAndResolveStatusPolicy {

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