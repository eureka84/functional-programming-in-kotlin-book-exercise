package org.eureka.kotlin.fp.ch8

import kotlin.math.max

data class SGen<A>(val forSize: (Int) -> Gen<A>) {
    operator fun invoke(i: Int): Gen<A> = forSize(i)
    fun <B> map(f: (A) -> B): SGen<B> = SGen { n -> forSize(n).map(f) }
    fun <B> flatMap(f: (A) -> Gen<B>): SGen<B> = SGen { n -> forSize(n).flatMap(f) }

    companion object {
        fun <A> nonEmptyListOf(ga: Gen<A>): SGen<List<A>> = SGen { n -> Gen.listOfN(max(1, n), ga) }
    }

}