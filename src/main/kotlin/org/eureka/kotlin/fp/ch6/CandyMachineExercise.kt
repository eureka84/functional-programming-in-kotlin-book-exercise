package org.eureka.kotlin.fp.ch6

import arrow.mtl.State
import arrow.mtl.StateApi
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
): State<Machine, Unit> = StateApi.fx() {
    val stateTransitions: List<State<Machine, Unit>> =
        inputs
            .map(machineTransitionGivenInput)
            .map(StateApi::modify)

    !stateTransitions.stateSequential()
}

private val machineTransitionGivenInput: (Input) -> (Machine) -> (Machine) =
    { i ->
        { s ->
            when (i) {
                is Coin ->
                    if (!s.locked || s.candies == 0) s
                    else Machine(false, s.candies, s.coins + 1)
                is Turn ->
                    if (s.locked || s.candies == 0) s
                    else Machine(true, s.candies - 1, s.coins)
            }
        }
    }


