package org.eureka.kotlin.fp.ch6

import arrow.core.extensions.IdMonad
import arrow.mtl.State
import arrow.mtl.StateApi.modify
import arrow.mtl.extensions.fx
import arrow.mtl.stateSequential

sealed class Input
object Coin : Input()
object Turn : Input()

data class Machine(
    val locked: Boolean,
    val candies: Int,
    val coins: Int
)

fun simulateMachine(
    inputs: List<Input>
): State<Machine, Unit> = State.fx(object : IdMonad {}) {
    val stateTransitions: List<State<Machine, Unit>> = inputs.map { i ->
        modify { m -> onInput(i, m) }
    }
    !stateTransitions.stateSequential()
}

//fun simulateMachine(
//    inputs: List<Input>
//): State<Machine, Unit> {
//    val stateTransitions: List<State<Machine, Unit>> =
//        inputs.map { i -> modify { m -> onInput(i, m) } }
//
//    val stateSequential: State<Machine, List<Unit>> = stateTransitions.stateSequential()
//
//    return stateSequential.map(idFunctor) {}
//}

private fun onInput(
    input: Input,
    m: Machine
): Machine = when {
    input is Coin && m.locked && m.candies > 0 ->
        m.copy(
            locked = false,
            coins = m.coins + 1
        )
    input is Turn && !m.locked ->
        m.copy(
            locked = true,
            candies = m.candies - 1,
            coins = m.coins
        )
    else -> m
}
