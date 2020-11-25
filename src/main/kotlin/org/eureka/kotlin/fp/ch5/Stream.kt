package org.eureka.kotlin.fp.ch5

import org.eureka.kotlin.fp.ch4.None
import org.eureka.kotlin.fp.ch4.Option
import org.eureka.kotlin.fp.ch4.Some
import org.eureka.kotlin.fp.ch3.List
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
            if (xs.isEmpty()) empty()
            else cons({ xs[0] },
                { of(*xs.sliceArray(1 until xs.size)) })
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