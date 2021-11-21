package org.eureka.kotlin.fp.ch3

import arrow.Kind

class ForTree
typealias TreeOf<A> = Kind<ForTree, A>

fun <A> TreeOf<A>.fix(): Tree<A> = this as Tree<A>

sealed class Tree<out A>: TreeOf<A> {

    companion object {

        fun <A> size(t: Tree<A>): Int = when (t) {
            is Leaf -> 1
            is Branch -> 1 + size(t.left) + size(t.right)
        }

        fun maximum(t: Tree<Int>): Int = when (t) {
            is Leaf -> t.value
            is Branch -> maxOf(maximum(t.left), maximum(t.right))
        }

        fun <A> depth(t: Tree<A>): Int = when (t) {
            is Leaf -> 1
            is Branch -> 1 + maxOf(depth(t.left), depth(t.right))
        }

        fun <A, B> map(t: Tree<A>, f: (A) -> B): Tree<B> = when (t) {
            is Leaf -> Leaf(f(t.value))
            is Branch -> Branch(map(t.left, f), map(t.right, f))
        }

        fun <A, B> fold(ta: Tree<A>, l: (A) -> B, b: (B, B) -> B): B = when (ta) {
            is Leaf -> l(ta.value)
            is Branch -> b(fold(ta.left, l, b), fold(ta.right, l, b))
        }

        fun <A> sizeF(ta: Tree<A>): Int =
            fold(ta, { 1 }, { l, r -> 1 + l + r })

        fun maximumF(ta: Tree<Int>): Int =
            fold(ta, { it }, { l, r -> maxOf(l, r) })

        fun <A> depthF(ta: Tree<A>): Int =
            fold(ta, { 1 }, { l, r -> 1 + maxOf(l, r) })

        fun <A, B> mapF(ta: Tree<A>, f: (A) -> B): Tree<B> =
            fold(ta, { leaf(f(it)) }, { l, r -> branch(l, r) })

        fun <A> leaf(a: A): Tree<A> = Leaf(a)

        fun <A> branch(l: Tree<A>, r: Tree<A>): Tree<A> = Branch(l, r)

    }

}

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(
    val left: Tree<A>,
    val right: Tree<A>
) : Tree<A>()