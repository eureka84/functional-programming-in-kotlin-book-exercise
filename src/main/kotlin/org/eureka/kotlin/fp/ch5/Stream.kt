package org.eureka.kotlin.fp.ch5

import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.*
import org.eureka.kotlin.fp.ch5.Stream.Companion.cons
import org.eureka.kotlin.fp.ch5.Stream.Companion.empty
import org.eureka.kotlin.fp.ch5.Stream.Companion.unfold
import org.eureka.kotlin.fp.ch3.Cons as LCons

sealed class Stream<out A> {

    companion object {

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

        fun <A, S> unfold(z: S, f: (S) -> Option<Pair<A, S>>): Stream<A> =
            f(z).map { (a: A, s: S) -> cons({ a }, { unfold(s, f) }) }.getOrElse { empty() }

    }
}

data class Cons<out A>(
    val head: () -> A,
    val tail: () -> Stream<A>
) : Stream<A>()

object Empty : Stream<Nothing>()

fun <A> Stream<A>.headOption(): Option<A> =
    this.foldRight({ Option.empty() }) { a, _ -> Option.of(a) }

fun <A> Stream<A>.toList(): List<A> {
    tailrec fun loop(acc: List<A>, rem: Stream<A>): List<A> =
        when (rem) {
            is Cons -> loop(LCons(rem.head(), acc), rem.tail())
            is Empty -> acc
        }
    return List.reverse(loop(List.empty(), this))
}

fun <A> Stream<A>.take(n: Int): Stream<A> =
    unfold(Pair(this, n)) { (s, toTake) ->
        when (s) {
            is Empty -> Option.empty()
            is Cons ->
                if (toTake == 0)
                    Option.empty()
                else
                    Option.of(Pair(s.head(), Pair(s.tail(), toTake - 1)))
        }
    }
//fun <A> Stream<A>.take(n: Int): Stream<A> {
//    fun go(xs: Stream<A>, n: Int): Stream<A> = when (xs) {
//        is Empty -> empty()
//        is Cons ->
//            if (n == 0) empty()
//            else cons(xs.head, { go(xs.tail(), n - 1) })
//    }
//    return go(this, n)
//}

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
    unfold(this) { s ->
        when (s) {
            is Empty -> Option.empty()
            is Cons ->
                if (p(s.head()))
                    Option.of(Pair(s.head(), s.tail()))
                else
                    Option.empty()
        }
    }
//    this.foldRight({ empty() }) { a, b -> if (p(a)) cons({ a }, b) else b() }

fun <A, B> Stream<A>.foldRight(z: () -> B, f: (A, () -> B) -> B): B =
    when (this) {
        is Cons -> f(this.head()) { tail().foldRight(z, f) }
        else -> z()
    }

fun <A> Stream<A>.forAll(p: (A) -> Boolean): Boolean =
    this.foldRight({ true }) { a, b -> p(a) && b() }

fun <A, B> Stream<A>.map(f: (A) -> B): Stream<B> =
    unfold(this) { s ->
        when (s) {
            is Empty -> Option.empty()
            is Cons -> Option.of(Pair(f(s.head()), s.tail()))
        }
    }
//    this.foldRight({ empty() }) { a, b -> cons({ f(a) }, b) }

fun <A> Stream<A>.filter(p: (A) -> Boolean): Stream<A> =
    this.foldRight({ empty() }) { a, b -> if (p(a)) cons({ a }, b) else b() }

fun <A> Stream<A>.find(p: (A) -> Boolean): Option<A> =
    filter(p).headOption()

fun <A, B> Stream<A>.flatMap(f: (A) -> Stream<B>): Stream<B> =
    this.foldRight({ empty() }) { a, b -> f(a).append(b) }

fun <A> Stream<A>.append(s: () -> Stream<A>): Stream<A> =
    this.foldRight(s) { a, b -> cons({ a }, b) }

fun <A, B, C> Stream<A>.zipWith(
    that: Stream<B>,
    f: (A, B) -> C
): Stream<C> =
    unfold(Pair(this, that)) { (s1, s2) ->
        when (s1) {
            is Empty -> Option.empty()
            is Cons -> when (s2) {
                is Empty -> Option.empty()
                is Cons -> Option.of(Pair(f(s1.head(), s2.head()), Pair(s1.tail(), s2.tail())))
            }
        }
    }

fun <A, B> Stream<A>.zipAll(
    that: Stream<B>
): Stream<Pair<Option<A>, Option<B>>> =
    unfold(Pair(this, that)) { (s1, s2) ->
        when (s1) {
            is Empty -> when (s2) {
                is Empty -> Option.empty()
                is Cons -> Option.of(
                    Pair(
                        Option.empty<A>() to Option.of(s2.head()),
                        s1 to s2.tail()
                    )
                )
            }
            is Cons -> when (s2) {
                is Empty -> Option.of(
                    Pair(
                        Option.of(s1.head()) to Option.empty(),
                        s1.tail() to s2
                    )
                )
                is Cons -> Option.of(
                    Pair(
                        Option.of(s1.head()) to Option.of(s2.head()),
                        s1.tail() to s2.tail()
                    )
                )
            }
        }
    }

fun <A> Stream<A>.startsWith(that: Stream<A>): Boolean =
    this.zipAll(that)
        .takeWhile { (_ , s: Option<A>) ->  !s.isEmpty() }
        .forAll { it.first == it.second }


fun <A> Stream<A>.tails(): Stream<Stream<A>> =
    unfold(this) { s ->
        when (s) {
            is Empty -> Option.empty()
            is Cons -> Option.of(Pair(s, s.tail()))
        }
    }