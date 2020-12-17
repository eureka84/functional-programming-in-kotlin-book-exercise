package org.eureka.kotlin.fp.ch8

import arrow.core.extensions.list.foldable.exists
import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch8.Prop.Companion.run
import org.junit.jupiter.api.Test

class PropExamplesTest {

    @Test
    internal fun `max prop`() {
        val smallInt = Gen.choose(-10, 10)

        val maxProp = Checkers.forAll(SGen.nonEmptyListOf(smallInt)) { ns: List<Int> ->
            val mx = ns.maxOrNull()!!
            !ns.exists { it > mx }
        }

        run(maxProp) shouldBe Passed
    }
}
