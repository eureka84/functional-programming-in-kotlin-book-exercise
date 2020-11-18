package org.eureka.kotlin.fp.ch3

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.eureka.kotlin.fp.ch3.List.Companion.tail
import org.junit.Test

class ListTest {

    @Test
    fun `tail of a non empty list`() {
        val list = List.of(1, 2, 3, 4)

        assertThat(tail(list)).isEqualTo(List.of(2, 3, 4))
    }

    @Test
    fun `tail of an empty list`() {
        assertThat { tail(Nil) }.isFailure().hasMessage("Tail of an empty list")
    }
}