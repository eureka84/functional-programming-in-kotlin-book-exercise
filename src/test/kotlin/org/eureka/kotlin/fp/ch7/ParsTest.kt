package org.eureka.kotlin.fp.ch7

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch7.Pars.asyncF
import org.eureka.kotlin.fp.ch7.Pars.parMap
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors.newCachedThreadPool

internal class ParsTest {

    @Test
    internal fun `asyncF test`() {
        val asyncSquare: (Int) -> Par<Int> = asyncF { a: Int -> a * a }

        asyncSquare(12)(newCachedThreadPool()).get() shouldBe 144
    }

    @Test
    internal fun `par map test`() {
        val listOfSquares = parMap(listOf(1, 2, 3, 4)) { it * it }

        listOfSquares(newCachedThreadPool()).get() shouldBe listOf(1, 4, 9, 16)
    }
}