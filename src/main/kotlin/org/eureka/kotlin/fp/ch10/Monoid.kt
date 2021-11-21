package org.eureka.kotlin.fp.ch10

import arrow.core.Option
import org.eureka.kotlin.fp.ch10.MonoidInstances.dual
import org.eureka.kotlin.fp.ch10.MonoidInstances.endoMonoid
import org.eureka.kotlin.fp.ch7.Par
import org.eureka.kotlin.fp.ch7.Pars
import org.eureka.kotlin.fp.ch7.splitAt

interface Monoid<A> {
    fun combine(a1: A, a2: A): A
    val nil: A
}

object MonoidInstances {
    fun intAddition(): Monoid<Int> = object : Monoid<Int> {
        override fun combine(a1: Int, a2: Int): Int = a1 + a2
        override val nil: Int = 0
    }

    fun intMultiplication(): Monoid<Int> = object : Monoid<Int> {
        override fun combine(a1: Int, a2: Int): Int = a1 * a2
        override val nil: Int = 1
    }

    fun booleanOr(): Monoid<Boolean> = object : Monoid<Boolean> {
        override val nil: Boolean = false
        override fun combine(a1: Boolean, a2: Boolean): Boolean = a1 || a2
    }

    fun booleanAnd(): Monoid<Boolean> = object : Monoid<Boolean> {
        override val nil: Boolean = true
        override fun combine(a1: Boolean, a2: Boolean): Boolean = a1 && a2
    }

    fun <A> optionMonoid(m: Monoid<A>): Monoid<Option<A>> = object : Monoid<Option<A>> {
        override fun combine(a1: Option<A>, a2: Option<A>): Option<A> = when {
            a1.isEmpty() -> a2
            a2.isEmpty() -> a1
            else -> a1.flatMap { a1val -> a2.map { a2val -> m.combine(a1val, a2val) } }
        }

        override val nil: Option<A> = Option.empty()
    }

    fun <A> dual(m: Monoid<A>): Monoid<A> = object : Monoid<A> {
        override fun combine(a1: A, a2: A): A = m.combine(a2, a1)
        override val nil: A = m.nil
    }

    fun <A> endoMonoid(): Monoid<(A) -> A> = object : Monoid<(A) -> A> {
        override fun combine(a1: (A) -> A, a2: (A) -> A): (A) -> A = { a -> a2(a1(a)) }
        override val nil: (A) -> A = { it }
    }
}

object MonoidExtensions {

    fun <A, B> foldMap(la: List<A>, m: Monoid<B>, f: (A) -> B): B =
//        la.map(f).foldRight(m.nil, m::combine)
        when {
            la.isEmpty() -> m.nil
            la.size == 1 -> f(la[0])
            else -> la.splitAt(la.size / 2).let { (leftHalf, rightHalf) ->
                m.combine(
                    foldMap(leftHalf, m, f),
                    foldMap(rightHalf, m, f),
                )
            }
        }

    fun <A, B> foldRight(la: List<A>, z: B, f: (A, B) -> B): B =
        foldMap(la, endoMonoid()) { a: A -> { b: B -> f(a, b) } }(z)

    fun <A, B> foldLeft(la: List<A>, z: B, f: (B, A) -> B): B =
        foldMap(la, dual(endoMonoid())) { a: A -> { b: B -> f(b, a) } }(z)

    fun <A> par(m: Monoid<A>): Monoid<Par<A>> = object : Monoid<Par<A>> {
        override fun combine(a1: Par<A>, a2: Par<A>): Par<A> =
            Pars.map2(a1, a2) { v1, v2 -> m.combine(v1, v2)}

        override val nil: Par<A>
            get() = Pars.lazyUnit { m.nil }

    }

    fun <A, B> parFoldMap(
        la: List<A>,
        pm: Monoid<Par<B>>,
        f: (A) -> B
    ): Par<B> =
        when {
            la.isEmpty() -> pm.nil
            la.size == 1 -> Pars.lazyUnit { f(la[0]) }
            else -> la.splitAt(la.size / 2).let { (leftHalf, rightHalf) ->
                pm.combine(
                    parFoldMap(leftHalf, pm, f),
                    parFoldMap(rightHalf, pm, f),
                )
            }
        }

    fun <A, B> productMonoid(
        ma: Monoid<A>,
        mb: Monoid<B>
    ): Monoid<Pair<A, B>> = object : Monoid<Pair<A, B>> {
        override fun combine(p1: Pair<A, B>, p2: Pair<A, B>): Pair<A, B> {
            val (a1, b1) = p1
            val (a2, b2) = p2
            return Pair(ma.combine(a1, a2), mb.combine(b1, b2))
        }

        override val nil: Pair<A, B> = Pair(ma.nil, mb.nil)
    }
}