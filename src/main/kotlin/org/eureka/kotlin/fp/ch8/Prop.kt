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

    fun or(other: Prop) = Prop { n, rng ->
        when (val prop = run(n, rng)) {
            is Falsified ->
                other.tag(prop.failure).run(n, rng)
            is Passed -> prop
        }
    }
    private fun tag(msg: String) = Prop { n, rng ->
        when (val prop = run(n, rng)) {
            is Falsified -> Falsified(
                "$msg: ${prop.failure}",
                prop.successes
            )
            is Passed -> prop
        }
    }

}