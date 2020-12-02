package org.eureka.kotlin.fp.ch5

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.Option
import org.junit.jupiter.api.Test

class StreamTest {

    @Test
    fun `head of a stream`() {
        assertThat(Stream.of(1, 2, 3).headOption()).isEqualTo(Option.of(1))
        assertThat(Stream.empty<Int>().headOption()).isEqualTo(Option.empty())
    }

    @Test
    fun `stream to list`() {
        assertThat(Stream.of(1, 2, 3, 4).toList()).isEqualTo(List.of(1, 2, 3, 4))
        assertThat(Stream.empty<Int>().toList()).isEqualTo(List.empty())
    }

    @Test
    fun `take n`() {
        assertThat(Stream.of(1, 2, 3, 4, 5, 6).take(3).toList()).isEqualTo(List.of(1, 2, 3))
        assertThat(Stream.empty<Any>().take(1)).isEqualTo(Stream.empty())
    }

    @Test
    fun `drop n`() {
        assertThat(Stream.of(1, 2, 3, 4, 5, 6).drop(3).toList()).isEqualTo(List.of(4, 5, 6))
        assertThat(Stream.empty<Any>().drop(1)).isEqualTo(Stream.empty())
    }

    @Test
    fun `take while`() {
        val p: (Int) -> Boolean = { it % 2 != 0 }
        assertThat(Stream.of(1, 3, 5, 6, 7).takeWhile(p).toList()).isEqualTo(List.of(1, 3, 5))
        assertThat(Stream.empty<Int>().takeWhile(p)).isEqualTo(Stream.empty())
    }

    @Test
    fun `for all`() {
        assertThat(Stream.of(1, 2, 3, 4).forAll { it < 5 }).isTrue()
    }

    @Test
    fun `map of a stream`() {
        assertThat(Stream.of(1, 2, 3).map { it * 2 }.toList()).isEqualTo(List.of(2, 4, 6))
    }

    @Test
    fun `filter elements`() {
        assertThat(Stream.of(1, 2, 3, 4, 5).filter { it % 2 == 0 }.toList()).isEqualTo(List.of(2, 4))
        assertThat(Stream.of(1, 3, 5).filter { it % 2 == 0 }.toList()).isEqualTo(List.empty())
    }

    @Test
    fun flatmap() {
        assertThat(Stream.of(1, 2, 3).flatMap { Stream.of(it, it) }.toList()).isEqualTo(List.of(1, 1, 2, 2, 3, 3))
    }

    @Test
    fun `find elem`() {
        assertThat(Stream.of(1, 2, 3, 4, 5).find { it % 2 == 0 }).isEqualTo(Option.of(2))
        assertThat(Stream.of(1, 3, 5).find { it % 2 == 0 }).isEqualTo(Option.empty())
    }

    @Test
    fun `zipWith should work`() {
        val s1 = Stream.of(1, 3, 4, 5)
        val s2 = Stream.of(1, 2, 3)
        val empty = Stream.empty<Int>()

        assertThat(s1.zipWith(s2, Int::plus).toList()).isEqualTo(List.of(2, 5, 7))
        assertThat(s1.zipWith(empty, Int::plus).toList()).isEqualTo(List.empty())
        assertThat(empty.zipWith(s2, Int::plus).toList()).isEqualTo(List.empty())
    }

    @Test
    fun `zipAll test`() {
        val s1 = Stream.of(1)
        val s2 = Stream.of(1, 2)
        val empty = Stream.empty<Int>()

        assertThat(s1.zipAll(s2).toList()).isEqualTo(
            List.of(
                Option.of(1) to Option.of(1),
                Option.empty<Int>() to Option.of(2)
            )
        )

        assertThat(s1.zipAll(empty).toList()).isEqualTo(
            List.of(
                Option.of(1) to Option.empty()
            )
        )
        assertThat(empty.zipAll(s2).toList()).isEqualTo(
            List.of(
                Option.empty<Int>() to Option.of(1),
                Option.empty<Int>() to Option.of(2)
            )
        )
    }

    @Test
    fun startsWith() {
        assertThat(Stream.of(1, 2, 3, 4, 5).startsWith(Stream.of(1, 2, 3))).isTrue()
        assertThat(Stream.of(1, 2).startsWith(Stream.of(1, 2, 3))).isFalse()

    }

    @Test
    fun tails() {
        val tails = Stream.of(1, 2, 3).tails().toList()
        val expected = List.of(
            List.of(1, 2, 3),
            List.of(2, 3),
            List.of(3),
            List.empty()
        )

        val pairs = List.zipWith(tails, expected) { a, b -> Pair(a.toList(), b) }

        assertThat(List.forAll(pairs) { (f, s) -> f == s }).isTrue()
    }

    @Test
    fun `scanRight test`() {
        assertThat(Stream.of(1, 3, 5).scanRight(0) { a, b -> a + b() }.toList()).isEqualTo(List.of(9, 8, 5, 0))
    }
}