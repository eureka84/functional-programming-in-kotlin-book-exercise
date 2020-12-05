package org.eureka.kotlin.fp.ch6

import arrow.core.Tuple2
import arrow.mtl.State

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
): State<Machine, Unit> =
    State { machine ->
        inputs.fold(Tuple2(machine, Unit)) { acc, input ->
            val (m, _) = acc

            when {
                input is Coin && m.locked && m.candies > 0 ->
                    Tuple2(m.copy(locked = false, coins = m.coins + 1), Unit)
                input is Turn && !m.locked ->
                    Tuple2(
                        m.copy(
                            locked = true,
                            candies = m.candies - 1,
                            coins = m.coins
                        ),
                        Unit
                    )
                else -> acc
            }
        }
    }