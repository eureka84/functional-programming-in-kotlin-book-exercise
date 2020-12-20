package org.eureka.kotlin.fp.ch8

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch8.Prop.Companion.run
import org.junit.jupiter.api.Test

class PropExamplesTest {

    @Test
    internal fun `max prop`() {
        val smallInt = Gen.choose(-10, 10)

        val maxProp = Checkers.forAll(SGen.nonEmptyListOf(smallInt)) { ns: List<Int> ->
            ns.maxOrNull()
                ?.let { max -> ns.none { it > max } }
                ?: false
        }

        run(maxProp) shouldBe Passed
    }
}
