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

        fun <A> empty(): List<A> = Nil

        fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> f(xs.head, foldRight(xs.tail, z, f))
            }

        fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B =
            foldLeft(xs,
                { b: B -> b },
                { g, a ->
                    { b ->
                        g(f(a, b))
                    }
                })(z)

        tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
            }

        fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
            foldRight(
                xs,
                { b: B -> b },
                { a, g ->
                    { b ->
                        g(f(b, a))
                    }
                })(z)

        fun <A> append(l1: List<A>, l2: List<A>): List<A> =
            foldRightL(l1, l2) { el, acc -> Cons(el, acc) }
//            when (xs) {
//                is Nil -> of(a)
//                is Cons -> Cons(xs.head, append(xs.tail, a))
//            }

        fun <A> concatenate(xss: List<List<A>>): List<A> =
            foldRight(xss, empty()) { curr, acc -> append(curr, acc)}

        fun <A> reverse(xs: List<A>): List<A> = foldLeft(xs, empty()) { acc, el -> Cons(el, acc) }

        fun <A> length(xs: List<A>): Int = foldLeft(xs, 0) { acc, _ -> acc + 1 }

        fun <A> tail(xs: List<A>): List<A> = when (xs) {
            is Nil -> throw UnsupportedOperationException("Tail of an empty list")
            is Cons -> xs.tail
        }

        fun <A> setHead(xs: List<A>, x: A): List<A> = when (xs) {
            is Nil -> throw UnsupportedOperationException("Cannot replace `head` of a Nil list")
            is Cons -> Cons(x, xs.tail)
        }

        tailrec fun <A> drop(l: List<A>, n: Int): List<A> =
            if (n == 0) l
            else when (l) {
                is Cons -> drop(l.tail, n - 1)
                is Nil -> throw IllegalStateException(
                    "Cannot drop more elements than in list"
                )
            }

        tailrec fun <A> dropWhile(l: List<A>, f: (A) -> Boolean): List<A> = when (l) {
            is Cons -> if (f(l.head)) dropWhile(l.tail, f) else l
            is Nil -> l
        }

        fun <A> init(l: List<A>): List<A> = when (l) {
            is Cons ->
                if (l.tail == Nil) Nil
                else Cons(l.head, init(l.tail))
            is Nil ->
                throw IllegalStateException("Cannot init Nil list")
        }

    }
}

object Nil : List<Nothing>()

data class Cons<out A>(
    val head: A,
    val tail: List<A>
) : List<A>()