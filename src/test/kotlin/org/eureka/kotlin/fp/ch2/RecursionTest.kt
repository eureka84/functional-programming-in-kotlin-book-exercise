package org.eureka.kotlin.fp.ch2

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RecursionTest {

    @Test
    fun fibonacci() {
        assertThat(`2_1_Recursion`.fibonacci(0), equalTo(1))
        assertThat(`2_1_Recursion`.fibonacci(1), equalTo(1))
        assertThat(`2_1_Recursion`.fibonacci(2), equalTo(2))
        assertThat(`2_1_Recursion`.fibonacci(3), equalTo(3))
        assertThat(`2_1_Recursion`.fibonacci(4), equalTo(5))
        assertThat(`2_1_Recursion`.fibonacci(5), equalTo(8))
    }
}