package org.eureka.kotlin.fp.ch8

import org.eureka.kotlin.fp.ch6.MyState
import org.eureka.kotlin.fp.ch6.RNG
import org.eureka.kotlin.fp.ch6.nonNegativeInt

data class Gen<A>(val sample: MyState<RNG, A>) {
    companion object {
        fun <A> unit(a: A): Gen<A> = Gen(MyState.unit(a))

        fun boolean(): Gen<Boolean> = Gen(
            MyState<RNG, Int> { rng -> nonNegativeInt.run(rng) }.map { i -> i % 2 == 0 }
        )

        fun <A> listOfN(n: Int, ga: Gen<A>): Gen<List<A>> = Gen(MyState { rng ->
            val (a, rng1) = ga.sample.run(rng)
            (1 until n).fold(Pair(listOf(a), rng1)) { (l, r), _ ->
                val (a1, rng2) = ga.sample.run(r)
                Pair(l + a1, rng2)
            }
        })

        fun choose(start: Int, stopExclusive: Int): Gen<Int> = Gen(
            MyState<RNG, Int> { rng -> nonNegativeInt.run(rng) }
                .map { i -> start + i % (stopExclusive - start) }
        )
    }
}

