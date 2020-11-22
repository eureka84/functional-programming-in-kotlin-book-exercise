package org.eureka.kotlin.fp.ch3

sealed class Tree<out A> {

    companion object {

        fun <A> size(t: Tree<A>): Int = when (t) {
            is Leaf -> 1
            is Branch -> 1 + size(t.left) + size(t.right)
        }

        fun maximum(t: Tree<Int>): Int = when (t) {
            is Leaf -> t.value
            is Branch -> maxOf(maximum(t.left), maximum(t.right))
        }

    }

}

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(
    val left: Tree<A>,
    val right: Tree<A>
) : Tree<A>()