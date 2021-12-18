package org.direct.applicationservice

class IllegalCommandException(override val cause: Throwable?) : Throwable(null, cause)