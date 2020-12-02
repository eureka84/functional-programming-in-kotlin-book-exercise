package org.eureka.kotlin.fp.ch3

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch3.List.Companion.append
import org.eureka.kotlin.fp.ch3.List.Companion.concatenate
import org.eureka.kotlin.fp.ch3.List.Companion.drop
import org.eureka.kotlin.fp.ch3.List.Companion.dropWhile
import org.eureka.kotlin.fp.ch3.List.Companion.empty
import org.eureka.kotlin.fp.ch3.List.Companion.filter
import org.eureka.kotlin.fp.ch3.List.Companion.flatMap
import org.eureka.kotlin.fp.ch3.List.Companion.foldLeft
import org.eureka.kotlin.fp.ch3.List.Companion.foldRight
import org.eureka.kotlin.fp.ch3.List.Companion.hasSubsequence
import org.eureka.kotlin.fp.ch3.List.Companion.init
import org.eureka.kotlin.fp.ch3.List.Companion.length
import org.eureka.kotlin.fp.ch3.List.Companion.map
import org.eureka.kotlin.fp.ch3.List.Companion.of
import org.eureka.kotlin.fp.ch3.List.Companion.reverse
import org.eureka.kotlin.fp.ch3.List.Companion.setHead
import org.eureka.kotlin.fp.ch3.List.Companion.tail
import org.eureka.kotlin.fp.ch3.List.Companion.zipWith
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ListTest {

    @Test
    fun `tail of a non empty list`() {
        val list = of(1, 2, 3, 4)

        tail(list) shouldBe of(2, 3, 4)
    }

    @Test
    fun `tail of an empty list`() {
        val exception = shouldThrow<UnsupportedOperationException> { tail(Nil) }

        exception.message shouldBe "Tail of an empty list"
    }

    @Test
    fun `set head of an empty list`() {
        val exception = shouldThrow<UnsupportedOperationException> { setHead(empty(), 1) }

        exception.message shouldBe "Cannot replace `head` of a Nil list"
    }

    @Test
    fun `set head of a non empty list`() {
        setHead(of(12, 2, 3, 4), 1) shouldBe of(1, 2, 3, 4)
    }

    @Test
    fun `drop n elements of an empty list`() {
        val exception = shouldThrow<IllegalStateException> { drop(Nil, 3) }

        exception.message shouldBe "Cannot drop more elements than in list"
    }

    @Test
    fun `drop n elements from a non empty list`() {
        drop(of(1, 2, 3, 4), 2) shouldBe of(3, 4)
    }

    @Test
    fun `drop while on an empty list`() {
        dropWhile(Nil) { true } shouldBe empty()
    }

    @Test
    fun `drop while on non empty list`() {
        dropWhile(of(1, 2, 3, 4)) { n -> n < 3 } shouldBe of(3, 4)
    }

    @Test
    fun `init of an empty list`() {
        val exception = shouldThrow<IllegalStateException> { init(Nil) }

        exception.message shouldBe "Cannot init Nil list"
    }

    @Test
    fun `init of a non empty list`() {
        init(of(1, 2, 3, 4, 5)) shouldBe of(1, 2, 3, 4)
    }

    @Test
    fun `fold a list using Cons`() {
        foldRight(
            of(1, 2, 3),
            empty<Int>(),
            { x, y -> Cons(x, y) }
        ) shouldBe of(1, 2, 3)

        foldLeft(
            of(1, 2, 3),
            empty<Int>(),
            { x, y -> Cons(y, x) }
        ) shouldBe of (3, 2, 1)
    }

    @Test
    fun `length of an empty string`() {
        length(Nil) shouldBe 0
    }

    @Test
    fun `length of a non empty string`() {
        length(of(1, 2, 3, 4)) shouldBe 4
    }

    @Test
    fun `reverse a list`() {
        reverse(Nil) shouldBe empty()
        reverse(of(1, 2, 3)) shouldBe of(3, 2, 1)
    }

    @Test
    fun `append to a list`() {
        append(Nil, of(1)) shouldBe of(1)
        append(of(1, 2, 3), of(4, 5)) shouldBe of(1, 2, 3, 4, 5)
    }

    @Test
    fun `concatenate test`() {
        concatenate(of(of(1, 2), of(3, 4), of(5, 6))) shouldBe of(1, 2, 3, 4, 5, 6)
    }

    @Test
    fun `map test`() {
        map(of(1, 2, 3)) { x -> x + 1 } shouldBe of(2, 3, 4)
    }

    @Test
    fun `filter elements out of  a list`() {
        filter(of(1, 2, 3, 4)) { it % 2 == 0 } shouldBe of(2, 4)
    }

    @Test
    fun `flatMap on a list`() {
        flatMap(of(1, 2, 3)) { i -> of(i, i) } shouldBe of(1, 1, 2, 2, 3, 3)
    }

    @Test
    fun `zipWith test`() {
        zipWith(of(1, 2, 3), of(4, 5, 6)) { a, b -> a + b } shouldBe of(5, 7, 9)
        zipWith(of(1, 2, 3), of(4, 5, 6, 7)) { a, b -> a + b } shouldBe of(5, 7, 9)
    }

    @Test
    fun `has subsequence test`() {
        assertTrue(hasSubsequence(of(1, 2, 3, 4), of(1, 2)))
        assertTrue(hasSubsequence(of(1, 2, 3, 4), Nil))
        assertTrue(hasSubsequence(Nil, Nil))
        assertTrue(hasSubsequence(of(1, 2, 3, 4), of(2, 3)))
        assertFalse(hasSubsequence(Nil, of(1, 2)))
        assertFalse(hasSubsequence(of(1, 2, 3, 4), of(5, 6)))

        assertTrue(hasSubsequence(of(1, 2, 3, 4, 5), of(2, 5)))
    }
}