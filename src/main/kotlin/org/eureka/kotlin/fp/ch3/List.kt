package org.eureka.kotlin.fp.ch3

import java.lang.UnsupportedOperationException

sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> =
            if (aa.isEmpty())
                Nil
            else {
                val tail = aa.sliceArray(1 until aa.size)
                Cons(aa[0], of(*tail))
            }

        fun <A> tail(xs: List<A>): List<A> = when (xs) {
            is Nil -> throw UnsupportedOperationException("Tail of an empty list")
            is Cons -> xs.tail
        }

        fun <A> setHead(xs: List<A>, x: A): List<A> = when (xs) {
            is Nil -> of(x)
            is Cons -> Cons(x, xs.tail)
        }
    }
}

object Nil : List<Nothing>()

data class Cons<out A>(
    val head: A,
    val tail: List<A>
) : List<A>()