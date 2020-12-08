package org.eureka.kotlin.fp.ch7

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch7.Pars.asyncF
import org.eureka.kotlin.fp.ch7.Pars.choice
import org.eureka.kotlin.fp.ch7.Pars.choiceMap
import org.eureka.kotlin.fp.ch7.Pars.choiceN
import org.eureka.kotlin.fp.ch7.Pars.parMap
import org.eureka.kotlin.fp.ch7.Pars.run
import org.eureka.kotlin.fp.ch7.Pars.unit
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

    @Test
    internal fun choice() {
        val result = choice(unit(true), unit(1), unit(2))
        run(newCachedThreadPool(), result).get() shouldBe 1
    }

    @Test
    internal fun `test choiceN`() {
        val result = choiceN(unit(0), listOf(unit(1), unit(2)))
        run(newCachedThreadPool(), result).get() shouldBe 1
    }

    @Test
    internal fun `choice map`() {
        val result = choiceMap(unit(1), mapOf( 1 to unit(1), 2 to unit(2)))
        run(newCachedThreadPool(), result).get() shouldBe 1
    }
}