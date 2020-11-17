package org.eureka.kotlin.fp.ch2

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RecursionTest {

    @Test
    fun fibonacci() {
        assertThat(Recursion.fibonacci(0), equalTo(1))
        assertThat(Recursion.fibonacci(1), equalTo(1))
        assertThat(Recursion.fibonacci(2), equalTo(2))
        assertThat(Recursion.fibonacci(3), equalTo(3))
        assertThat(Recursion.fibonacci(4), equalTo(5))
        assertThat(Recursion.fibonacci(5), equalTo(8))
    }
}