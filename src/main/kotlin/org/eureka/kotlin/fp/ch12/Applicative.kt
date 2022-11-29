package org.eureka.kotlin.fp.ch12

import org.eureka.kotlin.fp.ch11.Functor

import arrow.Kind
import org.eureka.kotlin.fp.ch2.Functions.curry

interface Applicative<F> : Functor<F> {
    fun <A> unit(a: A): Kind<F, A>

    fun <A, B> apply(
        fab: Kind<F, (A) -> B>,
        fa: Kind<F, A>
    ): Kind<F, B> // = map2(fa, fab) { a: A, f: (A) -> B -> f(a) }

    override fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B> =
        apply(unit(f), fa)

    fun <A, B, C> map2(
        fa: Kind<F, A>,
        fb: Kind<F, B>,
        f: (A, B) -> C
    ): Kind<F, C> = apply(apply(unit(curry(f)), fa), fb)

    fun <A, B, C, D> map3(
        fa: Kind<F, A>,
        fb: Kind<F, B>,
        fc: Kind<F, C>,
        f: (A, B, C) -> D
    ): Kind<F, D> = apply(apply(apply(unit(curry(f)), fa), fb), fc)

    fun <A, B> traverse(
        la: List<A>,
        f: (A) -> Kind<F, B>
    ): Kind<F, List<B>> =
        la.foldRight(
            unit(listOf())
        ) { a: A, acc: Kind<F, List<B>> ->
            map2(f(a), acc) { b: B, lb: List<B> -> listOf(b) + lb }
        }

    fun <A> sequence(lfa: List<Kind<F, A>>): Kind<F, List<A>> = traverse(lfa) { it }

    fun <A> replicateM(n: Int, ma: Kind<F, A>): Kind<F, List<A>> =
        sequence(List(n) { ma })

    fun <A, B> product(
        ma: Kind<F, A>,
        mb: Kind<F, B>
    ): Kind<F, Pair<A, B>> = map2(ma, mb) { a, b -> a to b }
}
