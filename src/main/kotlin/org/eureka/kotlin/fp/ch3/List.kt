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
        fun <A> cons(a: A, tl: List<A>): List<A> = Cons(a, tl)

        fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> f(xs.head, foldRight(xs.tail, z, f))
            }

        fun <A, B> foldRightL(xs: List<A>, z: B, f: (A, B) -> B): B =
            foldLeft(
                xs,
                { b: B -> b },
                { g: (B) -> B, a: A ->
                    { b: B ->
                        g(f(a, b))
                    }
                }
            )(z)

        tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
            }

        fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
            foldRight(
                xs,
                { b: B -> b },
                { a: A, g: (B) -> B ->
                    { b: B ->
                        g(f(b, a))
                    }
                }
            )(z)

        fun <A, B> map(xs: List<A>, f: (A) -> B): List<B> = foldRight(xs, empty()) { curr, acc -> Cons(f(curr), acc) }

        fun <A, B> flatMap(xa: List<A>, f: (A) -> List<B>): List<B> = concatenate(map(xa, f))

        fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> =
            flatMap(xs) { if (f(it)) of(it) else empty() }
//            foldRight(xs, empty()) { el, acc ->
//                if (f(el)) Cons(el, acc) else acc
//            }

        fun <A> append(l1: List<A>, l2: List<A>): List<A> =
            foldRightL(l1, l2) { el, acc -> Cons(el, acc) }

        fun <A> concatenate(xss: List<List<A>>): List<A> =
            foldRightL(xss, empty()) { curr, acc -> append(curr, acc) }

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

        fun <A, B, C> zipWith(xs: List<A>, ys: List<B>, f: (A, B) -> C): List<C> = when (xs) {
            is Nil -> Nil
            is Cons -> when (ys) {
                is Nil -> Nil
                is Cons -> Cons(f(xs.head, ys.head), zipWith(xs.tail, ys.tail, f))
            }
        }

        tailrec fun <A> hasSubsequence(xs: List<A>, ys: List<A>): Boolean = when (xs) {
            is Nil -> length(ys) == 0
            is Cons -> when (ys) {
                is Nil -> true
                is Cons ->
                    if (xs.head == ys.head)
                        hasSubsequence(xs.tail, ys.tail)
                    else
                        hasSubsequence(xs.tail, ys)
            }
        }

        fun <A> forAll(xs: List<A>, p: (A) -> Boolean) = foldRight(xs, true) { a, acc -> acc && p(a) }
    }
}

object Nil : List<Nothing>()

data class Cons<out A>(
    val head: A,
    val tail: List<A>
) : List<A>()