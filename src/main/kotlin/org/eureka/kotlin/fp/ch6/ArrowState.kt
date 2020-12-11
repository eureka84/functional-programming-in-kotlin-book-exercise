package org.eureka.kotlin.fp.ch6

import arrow.core.Tuple2
import arrow.mtl.*
import arrow.mtl.StateApi.get
import arrow.mtl.StateApi.set
import arrow.mtl.extensions.fx

object ArrowState {

    val int: State<RNG, Int> = State { rng ->
        val (i1, rng1) = rng.nextInt()
        Tuple2(rng1, if (i1 < 0) -(i1 + 1) else i1)
    }

    fun ints(x: Int): State<RNG, List<Int>> = (1..x).map { int }.stateSequential()

    fun <A, B> flatMap(
        s: State<RNG, A>,
        f: (A) -> State<RNG, B>
    ): State<RNG, B> = s.flatMap(f)

    fun <A, B> map(
        s: State<RNG, A>,
        f: (A) -> B
    ): State<RNG, B> = s.map(f)

    fun <S> modify(f: (S) -> S): State<S, Unit> =
        StateApi.fx() {
            val s: S = get<S>().bind()
            set(f(s)).bind()
        }

}