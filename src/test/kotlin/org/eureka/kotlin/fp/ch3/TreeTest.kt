package org.eureka.kotlin.fp.ch3

import io.kotest.matchers.shouldBe
import org.eureka.kotlin.fp.ch3.Tree.Companion.depth
import org.eureka.kotlin.fp.ch3.Tree.Companion.depthF
import org.eureka.kotlin.fp.ch3.Tree.Companion.map
import org.eureka.kotlin.fp.ch3.Tree.Companion.mapF
import org.eureka.kotlin.fp.ch3.Tree.Companion.maximum
import org.eureka.kotlin.fp.ch3.Tree.Companion.maximumF
import org.eureka.kotlin.fp.ch3.Tree.Companion.size
import org.eureka.kotlin.fp.ch3.Tree.Companion.sizeF
import org.junit.jupiter.api.Test

class TreeTest {

    private val t = Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))

    @Test
    fun `size test`() {
        size(t) shouldBe 5
        sizeF(t) shouldBe 5
    }

    @Test
    fun `maximum of`() {
        maximum(t) shouldBe 3
        maximumF(t) shouldBe 3
    }

    @Test
    fun `depth of`() {
        depth(t) shouldBe 3
        depthF(t) shouldBe 3
    }

    @Test
    fun mapTest() {
        val mapped = Branch(Branch(Leaf(2), Leaf(3)), Leaf(4))
        map(t) { it + 1} shouldBe mapped
        mapF(t) { it + 1} shouldBe mapped
    }
}