package org.eureka.kotlin.fp.ch8

import org.eureka.kotlin.fp.ch6.RNG

typealias SuccessCount = Int
typealias TestCases = Int
typealias FailedCase = String

sealed class Result {
    abstract fun isFalsified(): Boolean
}
object Passed : Result() {
    override fun isFalsified(): Boolean = false
}
data class Falsified(
    val failure: FailedCase,
    val successes: SuccessCount
) : Result() {
    override fun isFalsified(): Boolean = true
}

data class Prop(val check: (TestCases, RNG) -> Result)