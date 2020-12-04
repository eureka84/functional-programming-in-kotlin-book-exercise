package org.eureka.kotlin.fp.ch6

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll
import org.eureka.kotlin.fp.ch3.List

class SimpleRNGTest : StringSpec() {

    init {
        "non negatives" {
            forAll<Long> { seed ->
                nonNegativeInt(SimpleRNG(seed)).first >= 0
            }
        }

        "doubles in 0..1" {
            forAll<Long> {
                val double = double(SimpleRNG(it)).first
                double in 0.0..1.0
            }
        }

        "non negatives even" {
            forAll<Long> {
                val nonNegativeEven = nonNegativeEven(SimpleRNG(it)).first
                nonNegativeEven % 2 == 0 && nonNegativeEven >= 0
            }
        }

        "int double" {
            forAll<Long> {
                val (int, double) = intDouble(SimpleRNG(it)).first
                int >= 0 && double in 0.0..1.0
            }
        }

        "double int" {
            forAll<Long> {
                val (double, int) = doubleInt(SimpleRNG(it)).first
                int >= 0 && double in 0.0..1.0
            }
        }

        "double3" {
            forAll<Long> {
                val (d1, d2, d3) = double3(SimpleRNG(it)).first
                val range = 0.0..1.0
                d1 in range && d2 in range && d3 in range
            }
        }

        "ints" {
            forAll<Long> {
                val list = ints(5, SimpleRNG(it)).first
                List.forAll(list) { el -> el >= 0 }
            }
        }
    }
}