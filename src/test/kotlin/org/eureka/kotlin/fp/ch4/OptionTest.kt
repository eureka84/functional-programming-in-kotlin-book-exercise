package org.eureka.kotlin.fp.ch4

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.eureka.kotlin.fp.ch4.Option.Companion.empty
import org.eureka.kotlin.fp.ch4.Option.Companion.of
import org.junit.Test

class OptionTest {

    @Test
    fun `map over an option`() {
        val f: (Int) -> Int = { it * 2 }

        assertThat(of(1).map(f)).isEqualTo(of(2))
        assertThat(empty<Int>().map(f)).isEqualTo(empty())
    }

    @Test
    fun `get or else`() {
        val default = { 0 }

        assertThat(of(1).getOrElse(default)).isEqualTo(1)
        assertThat(empty<Int>().getOrElse(default)).isEqualTo(0)
    }

    @Test
    fun `or else`() {
        val ob = { of(0) }

        assertThat(of(1).orElse(ob)).isEqualTo(of(1))
        assertThat(empty<Int>().orElse(ob)).isEqualTo(of(0))
    }

    @Test
    fun `flat map`() {
        val inverse: (Double) -> Option<Double> = { if (it != 0.0) of(1 / it) else empty() }

        assertThat(of(1.0).flatMap(inverse)).isEqualTo(of(1.0))
        assertThat(of(0.0).flatMap(inverse)).isEqualTo(empty())
        assertThat(empty<Double>().flatMap(inverse)).isEqualTo(empty())
    }

    @Test
    fun filter() {
        val f: (Int) -> Boolean = { it % 2 == 0 }

        assertThat(of(1).filter(f)).isEqualTo(empty())
        assertThat(empty<Int>().filter(f)).isEqualTo(empty())
        assertThat(of(2).filter(f)).isEqualTo(of(2))
    }
}