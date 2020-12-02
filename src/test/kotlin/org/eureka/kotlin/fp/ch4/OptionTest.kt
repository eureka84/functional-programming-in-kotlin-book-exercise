package org.eureka.kotlin.fp.ch4

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.Option.Companion.catches
import org.eureka.kotlin.fp.ch4.Option.Companion.empty
import org.eureka.kotlin.fp.ch4.Option.Companion.lift
import org.eureka.kotlin.fp.ch4.Option.Companion.map2
import org.eureka.kotlin.fp.ch4.Option.Companion.of
import org.eureka.kotlin.fp.ch4.Option.Companion.sequence
import org.eureka.kotlin.fp.ch4.Option.Companion.traverse
import org.junit.jupiter.api.Test

class OptionTest {

    @Test
    fun `map over an option`() {
        val f: (Int) -> Int = { it * 2 }

        of(1).map(f) shouldBe of(2)
        empty<Int>().map(f) shouldBe empty()
    }

    @Test
    fun `get or else`() {
        val default = { 0 }

        of(1).getOrElse(default) shouldBe 1
        empty<Int>().getOrElse(default) shouldBe 0
    }

    @Test
    fun `or else`() {
        val ob = { of(0) }

        of(1).orElse(ob) shouldBe of(1)
        empty<Int>().orElse(ob) shouldBe of(0)
    }

    @Test
    fun `flat map`() {
        val inverse: (Double) -> Option<Double> = { if (it != 0.0) of(1 / it) else empty() }

        of(1.0).flatMap(inverse) shouldBe of(1.0)
        of(0.0).flatMap(inverse) shouldBe empty()
        empty<Double>().flatMap(inverse) shouldBe empty()
    }

    @Test
    fun filter() {
        val f: (Int) -> Boolean = { it % 2 == 0 }

        of(1).filter(f) shouldBe empty()
        empty<Int>().filter(f) shouldBe empty()
        of(2).filter(f) shouldBe of(2)
    }

    @Test
    fun `lift a function`() {
        val add1: (Int) -> Int = { it + 1 }
        val add1Opt: (Option<Int>) -> Option<Int> = lift(add1)

        add1Opt(of(1)) shouldBe of(2)
    }

    @Test
    fun `map2 test`() {
        val add: (Int, Int) -> Int = { a, b -> a + b }

        map2(of(2), of(3), add) shouldBe of(5)
    }

    @Test
    fun `sequence test`() {
        sequence(List.of(of(1), of(2), of(3))) shouldBe of(List.of(1, 2, 3))
        sequence(List.of(of(1), empty(), of(3))) shouldBe empty()
    }

    @Test
    fun `traverse test`() {
        val xa = List.of(1, 2, 4)
        val xb = List.of(0, 2, 4)
        val f: (Int) -> Option<Int> = { n -> catches { 1 / n } }

        traverse(xa, f) shouldBe of(List.of(1, 0, 0))
        traverse(xb, f) shouldBe empty()
    }
}