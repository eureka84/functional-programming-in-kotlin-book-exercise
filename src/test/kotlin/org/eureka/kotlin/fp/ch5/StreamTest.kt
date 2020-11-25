package org.eureka.kotlin.fp.ch5

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test
import org.eureka.kotlin.fp.ch3.List

class StreamTest {

    @Test
    fun `stream to list`() {
        assertThat(Stream.of(1, 2, 3, 4).toList()).isEqualTo(List.of(1, 2, 3, 4))
        assertThat(Stream.empty<Int>().toList()).isEqualTo(List.empty())
    }

}