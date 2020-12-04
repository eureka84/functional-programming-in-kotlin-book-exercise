package org.eureka.kotlin.fp.ch6

import arrow.core.extensions.IdMonad
import arrow.mtl.State
import arrow.mtl.extensions.fx
import arrow.mtl.run
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ArrowStateTest {

    private val idMonad: IdMonad = object : IdMonad {}

    @Test
    fun `state monad comprehension`() {
        val ns2: State<RNG, List<Int>> =
            State.fx(idMonad) {
                val x = !ArrowState.int
                val y = !ArrowState.int
                val xs: List<Int> = !ArrowState.ints(x)
                xs.map { it % y }
            }

        val rng: RNG = Fixed(2)

        val (_, listInt) = ns2.run(rng)

        listInt.size shouldBe 2
    }
}

class Fixed(private val n: Int) : RNG {
    override fun nextInt(): Pair<Int, RNG> {
        return n to this
    }
}
