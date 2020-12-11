package org.eureka.kotlin.fp.ch8

import org.eureka.kotlin.fp.ch6.MyState
import org.eureka.kotlin.fp.ch6.RNG
import org.eureka.kotlin.fp.ch6.nonNegativeInt

data class Gen<A>(val sample: MyState<RNG, A>) {
    companion object {
        fun <A> listOf(a: Gen<A>): List<Gen<A>> = TODO()

        fun <A> listOfN(n: Int, a: Gen<A>): List<Gen<A>> = TODO()

        fun <A> forAll(a: Gen<A>, f: (A) -> Boolean): Prop = TODO()

        fun choose(start: Int, stopExclusive: Int): Gen<Int> = Gen(
            MyState<RNG, Int> { rng -> nonNegativeInt.run(rng) }
                .map { i -> start + i % (stopExclusive - start) }
        )
    }
}