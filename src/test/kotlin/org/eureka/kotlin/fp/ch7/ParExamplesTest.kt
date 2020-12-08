package org.eureka.kotlin.fp.ch7

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch7.ParExamples.max
import org.eureka.kotlin.fp.ch7.ParExamples.sum
import org.eureka.kotlin.fp.ch7.Pars.run
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.Future

internal class ParExamplesTest {

    @Test
    internal fun `sum of empty list`() {
        val computation: Par<Int> = sum(listOf())

        val result: Future<Int> = run(Executors.newSingleThreadExecutor(), computation)

        result.get() shouldBe 0
    }

    @Test
    internal fun `sum of single element list`() {
        val computation: Par<Int> = sum(listOf(12))

        val result: Future<Int> = run(Executors.newSingleThreadExecutor(), computation)

        result.get() shouldBe 12
    }

    @Test
    internal fun `sum list parallel`() {
        val computation: Par<Int> = sum(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        val result: Future<Int> = run(Executors.newCachedThreadPool(), computation)

        result.get() shouldBe 55
    }

    @Test
    internal fun `max list parallel`() {
        val computation: Par<Int> = max(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        val result: Future<Int> = run(Executors.newCachedThreadPool(), computation)

        result.get() shouldBe 10
    }
}