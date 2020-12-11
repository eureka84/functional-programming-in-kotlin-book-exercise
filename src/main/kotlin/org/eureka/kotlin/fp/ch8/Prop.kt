package org.eureka.kotlin.fp.ch8

import arrow.core.Either

typealias SuccessCount = Int
typealias FailedCase = String

interface Prop {
    fun check(): Either<Pair<FailedCase, SuccessCount>, SuccessCount>
    fun and(p: Prop): Prop
}