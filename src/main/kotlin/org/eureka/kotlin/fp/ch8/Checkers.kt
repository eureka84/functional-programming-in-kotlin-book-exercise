package org.eureka.kotlin.fp.ch8

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.mtl.run
import org.eureka.kotlin.fp.ch6.RNG
import kotlin.math.min

object Checkers {

    fun <A> forAll(g: SGen<A>, f: (A) -> Boolean): Prop =
        forAll({ i -> g(i) }, f)

    fun <A> forAll(g: (Int) -> Gen<A>, f: (A) -> Boolean): Prop =
        Prop { max, n, rng ->
            val casePerSize: Int = (n + (max - 1)) / max
            val props: Sequence<Prop> =
                generateSequence(0) { it + 1 }
                    .take(min(n, max) + 1)
                    .map { i -> forAll(g(i), f) }
            val prop: Prop = props.map { p ->
                Prop { max, _, rng ->
                    p.check(max, casePerSize, rng)
                }
            }.reduce { p1, p2 -> p1.and(p2) }
            prop.check(max, n, rng)
        }

    fun <A> forAll(ga: Gen<A>, f: (A) -> Boolean): Prop =
        Prop { _, n: TestCases, rng: RNG ->
            randomSequence(ga, rng).mapIndexed { i, a ->
                try {
                    if (f(a)) Passed
                    else Falsified(a.toString(), i)
                } catch (e: Exception) {
                    Falsified(buildMessage(a, e), i)
                }
            }.take(n)
                .find { it.isFalsified() }
                .toOption()
                .getOrElse { Passed }
        }

    private fun <A> randomSequence(
        ga: Gen<A>,
        rng: RNG
    ): Sequence<A> =
        sequence {
            val (rng2: RNG, b: A) = ga.sample.run(rng)
            yield(b)
            yieldAll(randomSequence(ga, rng2))
        }

    private fun <A> buildMessage(a: A, e: Exception) =
        """
            |test case: $a
            |generated and exception: ${e.message}
            |stacktrace:
            |${e.stackTrace.joinToString("\n")}
        """.trimMargin()
}