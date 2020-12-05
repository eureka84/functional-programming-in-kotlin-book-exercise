package org.eureka.kotlin.fp.ch7

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch7.Pars.asyncF
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

internal class ParsTest {

    @Test
    internal fun `asyncF test`() {
        val asyncSquare: (Int) -> Par<Int> = asyncF { a: Int -> a * a }

        asyncSquare(12)(Executors.newCachedThreadPool()).get() shouldBe 144

    }
}