package org.eureka.kotlin.fp.ch2

import org.eureka.kotlin.fp.ch2.HigherOrderFunctions.isSorted
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HigherOrderFunctionsTest {

    private val unusedOrder: (Int, Int) -> Boolean = { _, _ -> true }

    @Test
    fun `isSorted - empty list`() {
        assertTrue {
            isSorted(listOf(), unusedOrder)
        }
    }

    @Test
    fun `isSorted - singleton list`() {
        assertTrue {
            isSorted(listOf(1), unusedOrder)
        }
    }

    @Test
    fun `isSorted - sorted list`() {
        assertTrue {
            isSorted(listOf(1, 2, 3, 5)) { a, b -> a <= b}
        }
    }

    @Test
    fun `isSorted - unsorted list`() {
        assertFalse {
            isSorted(listOf(1, 2, 3, 5, 4)) { a, b -> a <= b}
        }
    }

}