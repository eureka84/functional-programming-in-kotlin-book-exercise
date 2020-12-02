package org.eureka.kotlin.fp.ch2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class RecursionTest {

    @Test
    fun fibonacci() {
        Recursion.fibonacci(0) shouldBe 1
        Recursion.fibonacci(1) shouldBe 1
        Recursion.fibonacci(2) shouldBe 2
        Recursion.fibonacci(3) shouldBe 3
        Recursion.fibonacci(4) shouldBe 5
        Recursion.fibonacci(5) shouldBe 8
    }
}