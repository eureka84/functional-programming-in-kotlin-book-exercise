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

data class Prop(val run: (TestCases, RNG) -> Result) {

    private val self: Prop = this

    fun and(p: Prop): Prop = Prop { tc, rng ->
        when (val res = self.run(tc, rng)) {
            is Passed -> p.run(tc, rng)
            is Falsified -> res
        }
    }

    fun or(p: Prop): Prop = Prop { tc, rng ->
        val res1 = self.run(tc, rng)
        val res2 = p.run(tc, rng)
        when {
            res1 is Passed || res2 is Passed -> Passed
            res1 is Falsified -> res1
            else -> res2
        }
    }

}