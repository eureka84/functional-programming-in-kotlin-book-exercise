package org.eureka.kotlin.fp.ch2

import org.eureka.kotlin.fp.ch2.Functions.compose
import org.eureka.kotlin.fp.ch2.Functions.curry
import org.eureka.kotlin.fp.ch2.Functions.partial
import org.eureka.kotlin.fp.ch2.Functions.uncurry
import org.junit.Test
import kotlin.test.assertEquals

class FunctionTest {

    val sum = { a: Int, b: Int -> a + b }
    val prodCurried = { a: Int -> { b: Int -> a * b } }

    @Test
    fun test_partial() {
        val add2 = partial(2, sum)

        assertEquals(sum(2, 3), add2(3))
    }

    @Test
    fun test_curry() {
        val sumCurried = curry(sum)

        assertEquals(sumCurried(2)(3), sum(2, 3))
    }

    @Test
    fun test_uncurry() {
        val prod = uncurry(prodCurried)

        assertEquals(prodCurried(3)(5), prod(3, 5))
    }

    @Test
    fun test_compose() {
        val add3 = partial(3, sum)
        val multiplyBy2 = partial(2, uncurry(prodCurried))

        assertEquals(compose(add3, multiplyBy2)(2), 7)
    }
}