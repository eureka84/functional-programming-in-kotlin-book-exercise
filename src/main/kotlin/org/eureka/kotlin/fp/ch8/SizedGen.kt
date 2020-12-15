package org.eureka.kotlin.fp.ch8

data class SGen<A>(val forSize: (Int) -> Gen<A>) {
    operator fun invoke(i: Int): Gen<A> = this.forSize(i)
    fun <B> map(f: (A) -> B): SGen<B> = SGen { n -> this.forSize(n).map(f) }
    fun <B> flatMap(f: (A) -> Gen<B>): SGen<B> = SGen { n -> this.forSize(n).flatMap(f) }
}