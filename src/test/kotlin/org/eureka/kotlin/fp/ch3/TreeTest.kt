package org.eureka.kotlin.fp.ch3

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.eureka.kotlin.fp.ch3.Tree.Companion.maximum
import org.eureka.kotlin.fp.ch3.Tree.Companion.size
import org.junit.Test

class TreeTest {

    @Test
    fun `size test`() {
        assertThat(size(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)))).isEqualTo(5)
    }

    @Test
    fun `maximum of`() {
        assertThat(maximum(Branch(Branch(Leaf(1), Leaf(2)), Leaf(3)))).isEqualTo(3)
    }
}