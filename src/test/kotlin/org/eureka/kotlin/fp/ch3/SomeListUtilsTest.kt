package org.eureka.kotlin.fp.ch3

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.eureka.kotlin.fp.ch3.SomeListUtils.product
import org.eureka.kotlin.fp.ch3.SomeListUtils.sum
import org.junit.jupiter.api.Test

class SomeListUtilsTest {

    @Test
    fun `sum of a list`() {
        assertThat(sum(List.empty())).isEqualTo(0)
        assertThat(sum(List.of(1, 2, 3))).isEqualTo(6)
    }

    @Test
    fun `product of a list`() {
        assertThat(product(List.empty())).isEqualTo(1.0)
        assertThat(product(List.of(1.0, 2.0, 3.0))).isEqualTo(6.0)
    }
}