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

object Proved : Result()  {
    override fun isFalsified(): Boolean = false
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
            is Falsified -> res
            else -> p.check(maxSize, tc, rng)
        }
    }

    fun or(other: Prop) = Prop { maxSize, n, rng ->
        when (val prop = check(maxSize, n, rng)) {
            is Passed, is Proved -> prop
            is Falsified ->
                other.tag(prop.failure).check(maxSize, n, rng)
        }
    }

    private fun tag(msg: String) = Prop { maxSize, n, rng ->
        when (val prop = check(maxSize, n, rng)) {
            is Falsified -> Falsified("$msg: ${prop.failure}", prop.successes)
            else -> prop
        }
    }

    companion object {

        fun check(p: () -> Boolean): Prop =
            Prop { _, _, _ ->
                if (p()) Passed
                else Falsified("()", 0)
            }

        fun run(
            p: Prop,
            maxSize: Int = 100,
            testCases: Int = 100,
            rng: RNG = SimpleRNG(System.currentTimeMillis())
        ): Result =
            when (val result = p.check(maxSize, testCases, rng)) {
                is Falsified -> result.also {
                    println("Falsified after ${result.successes}" + "passed tests: ${result.failure}")
                }
                is Passed -> result.also {
                    println("OK, passed $testCases tests.")
                }
                is Proved -> result.also {
                    println("OK, property proved.")
                }
            }

    }

}