package org.eureka.kotlin.fp.ch3

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
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
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListTest {

    @Test
    fun `tail of a non empty list`() {
        val list = of(1, 2, 3, 4)

        assertThat(tail(list)).isEqualTo(of(2, 3, 4))
    }

    @Test
    fun `tail of an empty list`() {
        assertThat { tail(Nil) }.isFailure().hasMessage("Tail of an empty list")
    }

    @Test
    fun `set head of an empty list`() {
        assertThat { setHead(empty(), 1) }.isFailure().hasMessage("Cannot replace `head` of a Nil list")
    }

    @Test
    fun `set head of a non empty list`() {
        assertThat(setHead(of(12, 2, 3, 4), 1)).isEqualTo(of(1, 2, 3, 4))
    }

    @Test
    fun `drop n elements of an empty list`() {
        assertThat { drop(Nil, 3) }.isFailure().hasMessage("Cannot drop more elements than in list")
    }

    @Test
    fun `drop n elements from a non empty list`() {
        assertThat(drop(of(1, 2, 3, 4), 2)).isEqualTo(of(3, 4))
    }

    @Test
    fun `drop while on an empty list`() {
        assertThat(dropWhile(Nil) { true }).isEqualTo(empty())
    }

    @Test
    fun `drop while on non empty list`() {
        assertThat(dropWhile(of(1, 2, 3, 4)) { n -> n < 3 }).isEqualTo(of(3, 4))
    }

    @Test
    fun `init of an empty list`() {
        assertThat { init(Nil) }.isFailure().hasMessage("Cannot init Nil list")
    }

    @Test
    fun `init of a non empty list`() {
        assertThat(init(of(1, 2, 3, 4, 5))).isEqualTo(of(1, 2, 3, 4))
    }

    @Test
    fun `fold a list using Cons`() {
        assertThat(
            foldRight(
                of(1, 2, 3),
                empty<Int>(),
                { x, y -> Cons(x, y) })
        ).isEqualTo(of(1, 2, 3))

        assertThat(
            foldLeft(
                of(1, 2, 3),
                empty<Int>(),
                { x, y -> Cons(y, x) })
        ).isEqualTo(of(3, 2, 1))
    }

    @Test
    fun `length of an empty string`() {
        assertThat(length(Nil)).isEqualTo(0)
    }

    @Test
    fun `length of a non empty string`() {
        assertThat(length(of(1, 2, 3, 4))).isEqualTo(4)
    }

    @Test
    fun `reverse a list`() {
        assertThat(reverse(Nil)).isEqualTo(empty())
        assertThat(reverse(of(1, 2, 3))).isEqualTo(of(3, 2, 1))
    }

    @Test
    fun `append to a list`() {
        assertThat(append(Nil, of(1))).isEqualTo(of(1))
        assertThat(append(of(1, 2, 3), of(4, 5))).isEqualTo(of(1, 2, 3, 4, 5))
    }

    @Test
    fun `concatenate test`() {
        assertThat(concatenate(of(of(1, 2), of(3, 4), of(5, 6)))).isEqualTo(of(1, 2, 3, 4, 5, 6))
    }

    @Test
    fun `map test`() {
        assertThat(map(of(1, 2, 3)) { x -> x + 1 }).isEqualTo(of(2, 3, 4))
    }

    @Test
    fun `filter elements out of  a list`() {
        assertThat(filter(of(1, 2, 3, 4)) { it % 2 == 0 }).isEqualTo(of(2, 4))
    }

    @Test
    fun `flatMap on a list`() {
        assertThat(flatMap(of(1, 2, 3)) { i -> of(i, i) }).isEqualTo(of(1, 1, 2, 2, 3, 3))
    }

    @Test
    fun `zipWith test`() {
        assertThat(zipWith(of(1, 2, 3), of(4, 5, 6)) { a, b -> a + b }).isEqualTo(of(5, 7, 9))
        assertThat(zipWith(of(1, 2, 3), of(4, 5, 6, 7)) { a, b -> a + b }).isEqualTo(of(5, 7, 9))
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