package org.eureka.kotlin.fp.ch2

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FibTest {

    @Test
    fun test() {
        assertThat(Fib.fibonacci(0), equalTo(1))
        assertThat(Fib.fibonacci(1), equalTo(1))
        assertThat(Fib.fibonacci(2), equalTo(2))
        assertThat(Fib.fibonacci(3), equalTo(3))
        assertThat(Fib.fibonacci(4), equalTo(5))
        assertThat(Fib.fibonacci(5), equalTo(8))
    }
}