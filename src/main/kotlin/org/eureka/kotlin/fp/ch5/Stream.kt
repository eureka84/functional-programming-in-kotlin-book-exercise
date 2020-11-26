package org.eureka.kotlin.fp.ch5

import org.eureka.kotlin.fp.ch4.None
import org.eureka.kotlin.fp.ch4.Option
import org.eureka.kotlin.fp.ch4.Some
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch5.Stream.Companion.cons
import org.eureka.kotlin.fp.ch5.Stream.Companion.empty
import java.lang.IllegalStateException
import org.eureka.kotlin.fp.ch3.Cons as LCons

sealed class Stream<out A> {
    companion object {
        //smart constructors
        fun <A> cons(hd: () -> A, tl: () -> Stream<A>): Stream<A> {
            val head: A by lazy(hd)
            val tail: Stream<A> by lazy(tl)
            return Cons({ head }, { tail })
        }

        fun <A> empty(): Stream<A> = Empty

        fun <A> of(vararg xs: A): Stream<A> =
            if (xs.isEmpty()) {
                empty()
            } else {
                cons(
                    { xs[0] },
                    { of(*xs.sliceArray(1 until xs.size)) }
                )
            }
    }
}

data class Cons<out A>(
    val head: () -> A,
    val tail: () -> Stream<A>
) : Stream<A>()

object Empty : Stream<Nothing>()

fun <A> Stream<A>.headOption(): Option<A> =
    when (this) {
        is Empty -> None
        is Cons -> Some(head())
    }

fun <A> Stream<A>.toList(): List<A> {
    tailrec fun loop(acc: List<A>, rem: Stream<A>): List<A> =
        when (rem) {
            is Cons -> loop(LCons(rem.head(), acc), rem.tail())
            is Empty -> acc
        }
    return List.reverse(loop(List.empty(), this))
}

fun <A> Stream<A>.take(n: Int): Stream<A> {
    fun go(xs: Stream<A>, n: Int): Stream<A> = when (xs) {
        is Empty -> empty()
        is Cons ->
            if (n == 0) empty()
            else cons(xs.head, { go(xs.tail(), n - 1) })
    }
    return go(this, n)
}

fun <A> Stream<A>.drop(n: Int): Stream<A> {
    tailrec fun go(xs: Stream<A>, n: Int): Stream<A> = when (xs) {
        is Empty -> empty()
        is Cons ->
            if (n == 0) xs
            else go(xs.tail(), n - 1)
    }
    return go(this, n)
}

fun <A> Stream<A>.takeWhile(p: (A) -> Boolean): Stream<A> =
    when (this) {
        is Empty -> empty()
        is Cons ->
            if (p(this.head()))
                cons(this.head, { this.tail().takeWhile(p) })
            else
                empty()
    }