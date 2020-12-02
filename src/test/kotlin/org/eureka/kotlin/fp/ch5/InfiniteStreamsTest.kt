package org.eureka.kotlin.fp.ch5

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch5.InfiniteStreams.constant
import org.eureka.kotlin.fp.ch5.InfiniteStreams.fibs
import org.eureka.kotlin.fp.ch5.InfiniteStreams.from
import org.eureka.kotlin.fp.ch5.InfiniteStreams.ones
import org.junit.jupiter.api.Test

class InfiniteStreamsTest {

    @Test
    fun `infinite streams`() {
        ones().take(5).toList() shouldBe List.of(1, 1, 1, 1, 1)
        constant(1).take(5).toList() shouldBe List.of(1, 1, 1, 1, 1)
        from(1).take(5).toList() shouldBe List.of(1, 2, 3, 4, 5)
        fibs().take(5).toList() shouldBe List.of(0, 1, 1, 2, 3)
    }
}