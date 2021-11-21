package org.eureka.kotlin.fp.ch10

import arrow.Kind
import arrow.core.Option
import arrow.core.k
import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch10.FoldableExamples.bag
import org.eureka.kotlin.fp.ch3.Tree.Companion.branch
import org.eureka.kotlin.fp.ch3.Tree.Companion.leaf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class FoldableTest {

    @ParameterizedTest
    @MethodSource("fold")
    internal fun <F> fold(fa: Kind<F, Int>, foldable: Foldable<F>, f: (Int, Int) -> Int, expected: Int) {
        foldable.foldLeft(fa, 0, f) shouldBe expected
    }

    @ParameterizedTest
    @MethodSource("toList")
    internal fun <F> toList(fa: Kind<F, Int>, foldable: Foldable<F>, expected: List<Int>) {
        foldable.toList(fa) shouldBe expected
    }

    @Test
    internal fun `bag computation`() {
        bag(listOf("a", "a", "b", "c", "c", "c")) shouldBe mapOf("a" to 2, "b" to 1, "c" to 3)
    }

    companion object {
        @JvmStatic
        fun fold(): Stream<Arguments> {
            val f = { a: Int, b: Int -> a + b }
            return Stream.of(
                Arguments.of(branch(branch(leaf(1), leaf(2)), leaf(3)), TreeFoldable, f, 6),
                Arguments.of(listOf(1, 2, 3).k(), ListFoldable, f, 6),
                Arguments.of(Option.just(1), OptionFoldable, f, 1)
            )
        }

        @JvmStatic
        fun toList(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(branch(branch(leaf(1), leaf(2)), leaf(3)), TreeFoldable, listOf(3, 2, 1)),
                Arguments.of(listOf(1, 2, 3).k(), ListFoldable, listOf(1, 2, 3)),
                Arguments.of(Option.just(1), OptionFoldable, listOf(1))
            )
        }
    }

}