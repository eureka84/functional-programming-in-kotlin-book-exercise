package org.eureka.kotlin.fp.ch3

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch3.SomeListUtils.product
import org.eureka.kotlin.fp.ch3.SomeListUtils.sum
import org.junit.jupiter.api.Test

class SomeListUtilsTest {

    @Test
    fun `sum of a list`() {
        sum(List.empty()) shouldBe 0
        sum(List.of(1, 2, 3)) shouldBe 6
    }

    @Test
    fun `product of a list`() {
        product(List.empty()) shouldBe 1.0
        product(List.of(1.0, 2.0, 3.0)) shouldBe 6.0
    }
}