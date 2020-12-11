package org.eureka.kotlin.fp.ch8

import arrow.core.Tuple2
import arrow.core.extensions.IdFunctor
import arrow.core.toTuple2
import arrow.mtl.State
import arrow.mtl.StateApi.just
import arrow.mtl.run
import org.eureka.kotlin.fp.ch6.MyState
import org.eureka.kotlin.fp.ch6.RNG
import org.eureka.kotlin.fp.ch6.nonNegativeInt

data class Gen<A>(val sample: State<RNG, A>) {
    companion object {
        private val idFunctor = object : IdFunctor{}

        fun <A> unit(a: A): Gen<A> = Gen(just(a))

        fun boolean(): Gen<Boolean> = Gen(
            State<RNG, Int> { rng -> nonNegativeInt.run(rng).toTuple2().flip() }.map(idFunctor) { i -> i % 2 == 0 }
        )

        fun <A> listOfN(n: Int, ga: Gen<A>): Gen<List<A>> = Gen(
            State { rng ->
                val (rng1, a) = ga.sample.run(rng)
                (1 until n).fold(Tuple2(rng1, listOf(a))) { (r, l), _ ->
                    val run = ga.sample.run(r)
                    Tuple2(run.a, l + run.b)
                }
            })

        fun choose(start: Int, stopExclusive: Int): Gen<Int> = Gen(
            State<RNG, Int> { rng -> nonNegativeInt.run(rng).toTuple2().flip() }
                .map (idFunctor) { i -> start + i % (stopExclusive - start) }
        )

        private fun <A, B> Tuple2<A, B>.flip(): Tuple2<B, A> = Tuple2(this.b, this.a)
    }
}

