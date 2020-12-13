package org.eureka.kotlin.fp.ch8

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch6.SimpleRNG
import org.junit.jupiter.api.Test

class CheckersTest {

    @Test
    internal fun `for all`() {
        val gn = Gen.choose(1, 100)
        val listGen = gn.flatMap { a -> Gen.listOfN(gn, Gen.unit(a)) }
        val prop = Checkers.forAll(listGen) { l ->
            l.sum() == l.size * (l.getOrNull(0) ?: 0)
        }

        prop.run(100, SimpleRNG(2L)) shouldBe Passed
    }

    @Test
    internal fun `and props`() {
        val gn = Gen.choose(1, 100)
        val listGen = gn.flatMap { a -> Gen.listOfN(gn, Gen.unit(a)) }
        val prop =
            Checkers.forAll(listGen) { l ->
                l.sum() == l.size * (l.getOrNull(0) ?: 0)
            }.and(
                Checkers.forAll(listGen) { l ->
                    l.all { it == l[0] }
                }
            )

        prop.run(100, SimpleRNG(2L)) shouldBe Passed
    }

    @Test
    internal fun `or props`() {
        val gn = Gen.choose(1, 100)
        val listGen = gn.flatMap { a -> Gen.listOfN(gn, Gen.unit(a)) }
        val prop = Checkers.forAll(listGen) { l ->
            l.sum() == l.size * (l.getOrNull(0) ?: 0) + 1
        }.or(
            Checkers.forAll(listGen) { l ->
                l.all { it == l[0] }
            }
        )

        prop.run(100, SimpleRNG(2L)) shouldBe Passed
    }
}
