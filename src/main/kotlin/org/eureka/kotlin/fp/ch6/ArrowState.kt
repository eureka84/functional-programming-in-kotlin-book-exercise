package org.eureka.kotlin.fp.ch6

import arrow.core.Tuple2
import arrow.core.extensions.IdFunctor
import arrow.core.extensions.IdMonad
import arrow.mtl.State
import arrow.mtl.stateSequential

object ArrowState {

    private val idMonad: IdMonad = object : IdMonad {}
    private val idFunctor: IdFunctor = object : IdFunctor {}

    val int: State<RNG, Int> = State { rng ->
        val (i1, rng1) = rng.nextInt()
        Tuple2(rng1, if (i1 < 0) -(i1 + 1) else i1)
    }

    fun ints(x: Int): State<RNG, List<Int>> = (1..x).map { int }.stateSequential()

    fun <A, B> flatMap(
        s: State<RNG, A>,
        f: (A) -> State<RNG, B>
    ): State<RNG, B> = s.flatMap(idMonad, f)

    fun <A, B> map(
        s: State<RNG, A>,
        f: (A) -> B
    ): State<RNG, B> = s.map(idFunctor, f)

}