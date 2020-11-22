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

        fun <A> depth(t:Tree<A>): Int = when (t) {
            is Leaf -> 1
            is Branch -> 1 + maxOf(depth(t.left), depth(t.right))
        }

        fun <A, B> map(t: Tree<A>, f: (A) ->B): Tree<B> = when(t) {
            is Leaf -> Leaf(f(t.value))
            is Branch -> Branch(map(t.left, f), map(t.right, f))
        }

    }

}

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(
    val left: Tree<A>,
    val right: Tree<A>
) : Tree<A>()