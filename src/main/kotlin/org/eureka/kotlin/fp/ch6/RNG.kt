package org.eureka.kotlin.fp.ch6

import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch5.InfiniteStreams.from
import org.eureka.kotlin.fp.ch5.map
import org.eureka.kotlin.fp.ch5.take
import org.eureka.kotlin.fp.ch5.toList

typealias Rand<A> = MyState<RNG, A>

interface RNG {
    fun nextInt(): Pair<Int, RNG>
}

fun <A> unit(a: A): Rand<A> = MyState.unit(a)
fun <A, B> map(s: Rand<A>, f: (A) -> B): Rand<B> = s.map(f)
fun <A, B, C> map2(
    ra: Rand<A>,
    rb: Rand<B>,
    f: (A, B) -> C
): Rand<C> = MyState.map2(ra, rb, f)

fun <A, B> flatMap(f: Rand<A>, g: (A) -> Rand<B>): Rand<B> = f.flatMap(g)

fun <A> sequence(fs: List<Rand<A>>): Rand<List<A>> =
    List.foldLeft(fs, unit(List.empty())) { acc, r ->
        map2(acc, r) { l, a -> List.cons(a, l) }
    }

val nonNegativeInt: Rand<Int> = MyState { rng ->
    val (i1, rng2) = rng.nextInt()
    Pair(if (i1 < 0) -(i1 + 1) else i1, rng2)
}

val nonNegativeEven: Rand<Int> =
    map(nonNegativeInt) { it - (it % 2) }

val double: Rand<Double> =
    map(nonNegativeInt) { n -> n / (Int.MAX_VALUE.toDouble() + 1) }

val intDouble: Rand<Pair<Int, Double>> =
    map2(nonNegativeInt, double) { a, b -> Pair(a, b) }

val doubleInt: Rand<Pair<Double, Int>> =
    map2(double, nonNegativeInt) { a, b -> Pair(a, b) }

val double3: Rand<Triple<Double, Double, Double>> = MyState { rng ->
    val (d1, rng1) = double.run(rng)
    val (d2, rng2) = double.run(rng1)
    val (d3, rng3) = double.run(rng2)
    Pair(Triple(d1, d2, d3), rng3)
}

fun ints(count: Int, rng: RNG): Pair<List<Int>, RNG> {
    val rands: List<Rand<Int>> = from(1).take(count).map { nonNegativeInt }.toList()
    return sequence(rands).run(rng)
}

data class SimpleRNG(val seed: Long) : RNG {
    override fun nextInt(): Pair<Int, RNG> {
        val newSeed =
            (seed * 0x5DEECE66DL + 0xBL) and
                    0xFFFFFFFFFFFFL
        val nextRNG = SimpleRNG(newSeed)
        val n = (newSeed ushr 16).toInt()
        return Pair(n, nextRNG)
    }
}