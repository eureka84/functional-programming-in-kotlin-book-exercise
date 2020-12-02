package org.eureka.kotlin.fp.ch4

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.io.Serializable

class EitherTest {

    @Test
    fun `map test`() {
        val right: Either<String, Int> = Either.right(1)
        val left: Either<String, Int> = Either.left("OPS")
        val add1: (Int) -> Int = { it + 1 }

        assertThat(right.map(add1)).isEqualTo(Either.right(2))
        assertThat(left.map(add1)).isEqualTo(left)
    }

    @Test
    fun `flatMap test`() {
        val right = Either.right<String, Int>(2)
        val left = Either.left<String, Int>("OPS")
        val f: (Int) -> Either<Serializable, Int> = { Either.catches { 1 / it } }

        assertThat(right.flatMap(f)).isEqualTo(Either.right(0))
        assertThat(left.flatMap(f)).isEqualTo(left)
    }

    @Test
    fun `map2 test`() {
        val add: (Int, Int) -> Int = { a, b -> a + b }

        assertThat(Either.map2(Either.right<String, Int>(2), Either.right(3), add)).isEqualTo(Either.right(5))
    }

    @Test
    fun `orElse test`() {
        val right = Either.right<String, Int>(2)
        val left = Either.left<String, Int>("OPS")
        val default = Either.right<String, Int>(0)

        assertThat(right.orElse { default }).isEqualTo(right)
        assertThat(left.orElse { default }).isEqualTo(default)
    }

}