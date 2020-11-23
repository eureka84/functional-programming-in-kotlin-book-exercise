package org.eureka.kotlin.fp.ch4

import kotlin.math.pow

object Exercises {

    fun variance(xs: List<Double>): Option<Double> =
        mean(xs).flatMap { m ->
            mean(xs.map { x -> (x - m).pow(2) })
        }

    private fun mean(xs: List<Double>): Option<Double> = when {
        xs.isEmpty() -> Option.empty()
        else -> Option.of(xs.sum() / xs.size)
    }

}