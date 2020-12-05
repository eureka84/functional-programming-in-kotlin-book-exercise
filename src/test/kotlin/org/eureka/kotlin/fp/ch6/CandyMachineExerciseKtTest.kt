package org.eureka.kotlin.fp.ch6

import arrow.mtl.run
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CandyMachineExerciseKtTest {

    @Test
    internal fun `inserting coin into a locked machine`() {
        val machine = Machine(locked = true, candies = 10, coins = 0)
        val inputs = listOf<Input>(Coin)

        val (finalMachine, _) = simulateMachine(inputs).run(machine)

        finalMachine shouldBe Machine(locked = false, candies = 10, coins = 1)
    }

    @Test
    internal fun `turn the knob on an unlocked machine`() {
        val machine = Machine(locked = false, candies = 10, coins = 1)
        val inputs = listOf<Input>(Turn)

        val (finalMachine, _) = simulateMachine(inputs).run(machine)

        finalMachine shouldBe  Machine(locked = true, candies = 9, coins = 1)
    }

    @Test
    internal fun `turn the knob on a locked machine`() {
        val machine = Machine(locked = true, candies = 10, coins = 1)
        val inputs = listOf<Input>(Turn)

        val (finalMachine, _) = simulateMachine(inputs).run(machine)

        finalMachine shouldBe  machine
    }

    @Test
    internal fun `insert coin in an unlocked machine`() {
        val machine = Machine(locked = false, candies = 10, coins = 0)
        val inputs = listOf<Input>(Coin)

        val (finalMachine, _) = simulateMachine(inputs).run(machine)

        finalMachine shouldBe machine
    }

    @Test
    internal fun `a machine out of candies ignores all inputs`() {
        val machine = Machine(locked = true, candies = 0, coins = 0)
        val inputs = listOf(Coin, Turn, Coin, Turn)

        val (finalMachine, _) = simulateMachine(inputs).run(machine)

        finalMachine shouldBe machine
    }

}