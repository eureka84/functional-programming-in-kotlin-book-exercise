package org.eureka.kotlin.fp.ch3

import org.eureka.kotlin.fp.ch3.List.Companion.foldLeft

object SomeListUtils {

    fun sum(xs: List<Int>): Int = foldLeft(xs, 0) { el, acc -> acc + el }

    fun product(xs: List<Double>): Double = foldLeft(xs, 1.0) { el, acc -> el * acc }

}