package org.eureka.kotlin.fp.ch8

import arrow.core.Tuple2
import arrow.core.toTuple2
import arrow.mtl.State
import arrow.mtl.StateApi.just
import arrow.mtl.map
import arrow.mtl.stateSequential
import org.eureka.kotlin.fp.ch6.RNG
import org.eureka.kotlin.fp.ch6.nonNegativeInt

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {

        fun <A> unit(a: A): Gen<A> = Gen(just(a))

        fun boolean(): Gen<Boolean> = Gen(
            State<RNG, Int> { rng ->
                nonNegativeInt.run(rng).toTuple2().flip()
            }.map { i -> i % 2 == 0 }
        )

        fun <A> listOfN(n: Int, ga: Gen<A>): Gen<List<A>> = Gen(
            (1..n).map { ga.sample }.stateSequential()
        )

        fun choose(start: Int, stopExclusive: Int): Gen<Int> = Gen(
            State<RNG, Int> { rng ->
                nonNegativeInt.run(rng).toTuple2().flip()
            }.map { i -> start + i % (stopExclusive - start) }
        )

        private fun <A, B> Tuple2<A, B>.flip(): Tuple2<B, A> = Tuple2(this.b, this.a)
    }
}

