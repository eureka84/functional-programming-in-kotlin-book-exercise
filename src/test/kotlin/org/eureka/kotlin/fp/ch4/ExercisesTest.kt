package org.eureka.kotlin.fp.ch4

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch4.Exercises.variance
import org.junit.jupiter.api.Test

class ExercisesTest {

    @Test
    fun `variance of an empty list`() {
        variance(emptyList()) shouldBe Option.empty()
    }

    @Test
    fun `variance of a non empty list`() {
        variance(listOf(21.3, 38.4, 12.7, 41.6)) shouldBe Option.of(142.775)
    }
}