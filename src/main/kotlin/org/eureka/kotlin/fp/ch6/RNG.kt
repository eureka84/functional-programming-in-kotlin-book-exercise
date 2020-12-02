package org.eureka.kotlin.fp.ch6

import kotlin.math.abs

interface RNG {
    fun nextInt(): Pair<Int, RNG>
}

fun nonNegativeInt(rng: RNG): Pair<Int, RNG> =
    rng.nextInt().let { (n, nextRng) ->
        val nonNeg = if (n == Int.MIN_VALUE) Int.MAX_VALUE else abs(n)
        Pair(nonNeg, nextRng)
    }

fun double(rng: RNG): Pair<Double, RNG> =
    nonNegativeInt(rng).let { (n, nextRNG) ->
        val d = when (n) {
            0, 1 -> Int.MAX_VALUE.toDouble()
            in 0..1 -> n.toDouble()
            else -> 1.0 / n.toDouble()
        }
        Pair(d, nextRNG)
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