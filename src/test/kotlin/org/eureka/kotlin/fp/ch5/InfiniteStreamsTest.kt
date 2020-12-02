package org.eureka.kotlin.fp.ch5

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch5.InfiniteStreams.constant
import org.eureka.kotlin.fp.ch5.InfiniteStreams.fibs
import org.eureka.kotlin.fp.ch5.InfiniteStreams.from
import org.eureka.kotlin.fp.ch5.InfiniteStreams.ones
import org.junit.jupiter.api.Test

class InfiniteStreamsTest {

    @Test
    fun `infinite streams`() {
        assertThat(ones().take(5).toList()).isEqualTo(List.of(1, 1, 1, 1, 1))
        assertThat(constant(1).take(5).toList()).isEqualTo(List.of(1, 1, 1, 1, 1))
        assertThat(from(1).take(5).toList()).isEqualTo(List.of(1, 2, 3, 4, 5))
        assertThat(fibs().take(5).toList()).isEqualTo(List.of(0, 1, 1, 2, 3))
    }
}