package org.eureka.kotlin.fp.ch8

import org.eureka.kotlin.fp.ch6.RNG
import org.eureka.kotlin.fp.ch6.SimpleRNG

typealias SuccessCount = Int
typealias TestCases = Int
typealias FailedCase = String
typealias MaxSize = Int

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

data class Prop(val check: (MaxSize, TestCases, RNG) -> Result) {

    private val self: Prop = this

    fun and(p: Prop): Prop = Prop { maxSize, tc, rng ->
        when (val res = self.check(maxSize, tc, rng)) {
            is Passed -> p.check(maxSize, tc, rng)
            is Falsified -> res
        }
    }

    fun or(other: Prop) = Prop { maxSize, n, rng ->
        when (val prop = check(maxSize, n, rng)) {
            is Falsified ->
                other.tag(prop.failure).check(maxSize, n, rng)
            is Passed -> prop
        }
    }

    private fun tag(msg: String) = Prop { maxSize, n, rng ->
        when (val prop = check(maxSize, n, rng)) {
            is Falsified -> Falsified(
                "$msg: ${prop.failure}",
                prop.successes
            )
            is Passed -> prop
        }
    }

    companion object {

        fun run(
            p: Prop,
            maxSize: Int = 100,
            testCases: Int = 100,
            rng: RNG = SimpleRNG(System.currentTimeMillis())
        ): Result =
            when (val result = p.check(maxSize, testCases, rng)) {
                is Falsified -> result.also {
                    println(
                        "Falsified after ${result.successes}" +
                                "passed tests: ${result.failure}"
                    )
                }
                is Passed -> result.also {
                    println("OK, passed $testCases tests.")
                }
            }

    }

}