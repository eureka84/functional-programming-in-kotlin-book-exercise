package org.eureka.kotlin.fp.ch6

import arrow.mtl.State
import arrow.mtl.StateApi
import arrow.mtl.extensions.fx
import arrow.mtl.run
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ArrowStateTest {

    @Test
    fun `state monad comprehension`() {
        val ns2: State<RNG, List<Int>> =
            StateApi.fx() {
                val x = !ArrowState.int
                val y = !ArrowState.int
                val xs: List<Int> = !ArrowState.ints(x)
                xs.map { it % y }
            }

        val rng: RNG = Fixed(2)

        val (_, listInt) = ns2.run(rng)

        listInt.size shouldBe 2
    }

    @Test
    fun `modify state`() {
        val state: State<Int, Unit> = ArrowState.modify { i -> i + 1 }

        state.run(1).a shouldBe 2
    }

}

class Fixed(private val n: Int) : RNG {
    override fun nextInt(): Pair<Int, RNG> {
        return n to this
    }
}
