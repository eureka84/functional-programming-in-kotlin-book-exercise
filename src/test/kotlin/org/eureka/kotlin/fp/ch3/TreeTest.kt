package org.eureka.kotlin.fp.ch3

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.eureka.kotlin.fp.ch3.Tree.Companion.depth
import org.eureka.kotlin.fp.ch3.Tree.Companion.map
import org.eureka.kotlin.fp.ch3.Tree.Companion.maximum
import org.eureka.kotlin.fp.ch3.Tree.Companion.size
import org.junit.Test

class TreeTest {

    private val t = Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))

    @Test
    fun `size test`() {
        assertThat(size(t)).isEqualTo(5)
    }

    @Test
    fun `maximum of`() {
        assertThat(maximum(t)).isEqualTo(3)
    }

    @Test
    fun `depth of`() {
        assertThat(depth(t)).isEqualTo(3)
    }

    @Test
    fun mapTest() {
        val mapped = Branch(Branch(Leaf(2), Leaf(3)), Leaf(4))
        assertThat(map(t) { it + 1}).isEqualTo(mapped)
    }
}