package org.eureka.kotlin.fp.ch4

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.eureka.kotlin.fp.ch4.Exercises.variance
import org.junit.jupiter.api.Test

class ExercisesTest {

    @Test
    fun `variance of an empty list`() {
        assertThat(variance(emptyList())).isEqualTo(Option.empty())
    }

    @Test
    fun `variance of a non empty list`() {
        assertThat(variance(listOf(21.3, 38.4, 12.7, 41.6))).isEqualTo(Option.of(142.775))
    }
}