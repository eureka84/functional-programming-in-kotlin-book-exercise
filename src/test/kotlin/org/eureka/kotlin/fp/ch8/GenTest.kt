package org.eureka.kotlin.fp.ch8

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.forAll
import org.eureka.kotlin.fp.ch6.SimpleRNG

class GenTest : StringSpec() {
    init {
        "Gen chose should produce always a random number in the given range" {
            forAll(Arb.long(), Arb.int(), Arb.int()) { seed, a, b ->
                if (a == b) true
                else {
                    val (start, end) = if (a < b) Pair(a, b) else Pair(b, a)
                    val choose: Gen<Int> = Gen.choose(start, end)
                    choose.sample.run(SimpleRNG(seed)).first in start until end
                }
            }
        }

        "unit" {
            forAll<Pair<Long, Long>> { (seed, unit) ->
                Gen.unit(unit).sample.run(SimpleRNG(seed)).first == unit
            }
        }

        "listOfN" {
            forAll<Long> { seed ->
                val list = Gen.listOfN(10, Gen.choose(1, 100)).sample.run(SimpleRNG(seed)).first
                list.size == 10 && list.all { it in 1 until 100 }
            }
        }
    }
}
