package org.eureka.kotlin.fp.ch4

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.Serializable

class EitherTest {

    @Test
    fun `map test`() {
        val right: Either<String, Int> = Either.right(1)
        val left: Either<String, Int> = Either.left("OPS")
        val add1: (Int) -> Int = { it + 1 }

        right.map(add1) shouldBe Either.right(2)
        left.map(add1) shouldBe left
    }

    @Test
    fun `flatMap test`() {
        val right = Either.right<String, Int>(2)
        val left = Either.left<String, Int>("OPS")
        val f: (Int) -> Either<Serializable, Int> = { Either.catches { 1 / it } }

        right.flatMap(f) shouldBe Either.right(0)
        left.flatMap(f) shouldBe left
    }

    @Test
    fun `map2 test`() {
        val add: (Int, Int) -> Int = { a, b -> a + b }

        Either.map2(Either.right<String, Int>(2), Either.right(3), add) shouldBe Either.right(5)
    }

    @Test
    fun `orElse test`() {
        val right = Either.right<String, Int>(2)
        val left = Either.left<String, Int>("OPS")
        val default = Either.right<String, Int>(0)

        right.orElse { default } shouldBe right
        left.orElse { default } shouldBe default
    }

}