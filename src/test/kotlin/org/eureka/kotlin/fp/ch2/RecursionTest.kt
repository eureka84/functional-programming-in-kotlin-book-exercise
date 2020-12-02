package org.eureka.kotlin.fp.ch2

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test


class RecursionTest {

    @Test
    fun fibonacci() {
        assertThat(Recursion.fibonacci(0)).isEqualTo(1)
        assertThat(Recursion.fibonacci(1)).isEqualTo(1)
        assertThat(Recursion.fibonacci(2)).isEqualTo(2)
        assertThat(Recursion.fibonacci(3)).isEqualTo(3)
        assertThat(Recursion.fibonacci(4)).isEqualTo(5)
        assertThat(Recursion.fibonacci(5)).isEqualTo(8)
    }
}