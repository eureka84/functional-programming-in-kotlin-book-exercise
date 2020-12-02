package org.eureka.kotlin.fp.ch6

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll

class SimpleRNGTest: StringSpec() {

    init {
        "non negatives" {
            forAll<Long> { seed ->
                nonNegativeInt(SimpleRNG(seed)).first  >= 0
            }
        }
    }

}