package org.eureka.kotlin.fp.ch3

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.eureka.kotlin.fp.ch3.List.Companion.drop
import org.eureka.kotlin.fp.ch3.List.Companion.of
import org.eureka.kotlin.fp.ch3.List.Companion.setHead
import org.eureka.kotlin.fp.ch3.List.Companion.tail
import org.junit.Test

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
        assertThat(setHead(Nil, 1)).isEqualTo(of(1))
    }

    @Test
    fun `set head of a non empty list`() {
        assertThat(setHead(of(12, 2, 3, 4), 1)).isEqualTo(of(1, 2, 3, 4))
    }

    @Test
    fun `drop n elements of an empty list`() {
        assertThat { drop(Nil, 3) }.isFailure().hasMessage("Dropping elements from an empty list")
    }

    @Test
    fun `drop n elements from a non empty list`() {
        assertThat(drop(of(1, 2, 3, 4), 2)).isEqualTo(of(3, 4))
    }
}