package org.eureka.kotlin.fp.ch5

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.Option
import org.junit.jupiter.api.Test

class StreamTest {

    @Test
    fun `head of a stream`() {
        Stream.of(1, 2, 3).headOption() shouldBe Option.of(1)
        Stream.empty<Int>().headOption() shouldBe Option.empty()
    }

    @Test
    fun `stream to list`() {
        Stream.of(1, 2, 3, 4).toList() shouldBe List.of(1, 2, 3, 4)
        Stream.empty<Int>().toList() shouldBe List.empty()
    }

    @Test
    fun `take n`() {
        Stream.of(1, 2, 3, 4, 5, 6).take(3).toList() shouldBe List.of(1, 2, 3)
        Stream.empty<Any>().take(1) shouldBe Stream.empty()
    }

    @Test
    fun `drop n`() {
        Stream.of(1, 2, 3, 4, 5, 6).drop(3).toList() shouldBe List.of(4, 5, 6)
        Stream.empty<Any>().drop(1) shouldBe Stream.empty()
    }

    @Test
    fun `take while`() {
        val p: (Int) -> Boolean = { it % 2 != 0 }
        Stream.of(1, 3, 5, 6, 7).takeWhile(p).toList() shouldBe List.of(1, 3, 5)
        Stream.empty<Int>().takeWhile(p) shouldBe Stream.empty()
    }

    @Test
    fun `for all`() {
        Stream.of(1, 2, 3, 4).forAll { it < 5 } shouldBe true
    }

    @Test
    fun `map of a stream`() {
        Stream.of(1, 2, 3).map { it * 2 }.toList() shouldBe List.of(2, 4, 6)
    }

    @Test
    fun `filter elements`() {
        Stream.of(1, 2, 3, 4, 5).filter { it % 2 == 0 }.toList() shouldBe List.of(2, 4)
        Stream.of(1, 3, 5).filter { it % 2 == 0 }.toList() shouldBe List.empty()
    }

    @Test
    fun flatmap() {
        Stream.of(1, 2, 3).flatMap { Stream.of(it, it) }.toList() shouldBe List.of(1, 1, 2, 2, 3, 3)
    }

    @Test
    fun `find elem`() {
        Stream.of(1, 2, 3, 4, 5).find { it % 2 == 0 } shouldBe Option.of(2)
        Stream.of(1, 3, 5).find { it % 2 == 0 } shouldBe Option.empty()
    }

    @Test
    fun `zipWith should work`() {
        val s1 = Stream.of(1, 3, 4, 5)
        val s2 = Stream.of(1, 2, 3)
        val empty = Stream.empty<Int>()

        s1.zipWith(s2, Int::plus).toList() shouldBe List.of(2, 5, 7)
        s1.zipWith(empty, Int::plus).toList() shouldBe List.empty()
        empty.zipWith(s2, Int::plus).toList() shouldBe List.empty()
    }

    @Test
    fun `zipAll test`() {
        val s1 = Stream.of(1)
        val s2 = Stream.of(1, 2)
        val empty = Stream.empty<Int>()

        s1.zipAll(s2).toList() shouldBe
            List.of(
                Option.of(1) to Option.of(1),
                Option.empty<Int>() to Option.of(2)
            )


        s1.zipAll(empty).toList() shouldBe
            List.of(
                Option.of(1) to Option.empty()
            )

        empty.zipAll(s2).toList() shouldBe
            List.of(
                Option.empty<Int>() to Option.of(1),
                Option.empty<Int>() to Option.of(2)
            )

    }

    @Test
    fun startsWith() {
        Stream.of(1, 2, 3, 4, 5).startsWith(Stream.of(1, 2, 3)) shouldBe true
        Stream.of(1, 2).startsWith(Stream.of(1, 2, 3)) shouldBe false

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

        List.forAll(pairs) { (f, s) -> f == s } shouldBe true
    }

    @Test
    fun `scanRight test`() {
        Stream.of(1, 3, 5).scanRight(0) { a, b -> a + b() }.toList() shouldBe List.of(9, 8, 5, 0)
    }
}