package org.eureka.kotlin.fp.ch8

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.forAll

class SumOfAListTest: StringSpec() {
    init {
        val intList: Arb<List<Int>> = Arb.list(Arb.int(1..100), 1..100)
        "sum of a list should be equal to sum of list reversed" {
            forAll(intList) { l: List<Int> ->
                l.sum() == l.reversed().sum()
            }
        }

        "sum of a list should be equal to sum of list sorted" {
            forAll(intList) { l: List<Int> ->
                l.sum() == l.sorted().sum()
            }
        }

        "sum of a list of same element" {
            val list = Arb.list(Arb.constant(1), 1..100)
            forAll(list) { l ->
                l.sum() == l.size
            }
        }
    }
}