package org.eureka.kotlin.fp.ch11

import arrow.Kind
import arrow.core.*
import org.eureka.kotlin.fp.ch3.*
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.ForOption
import org.eureka.kotlin.fp.ch4.Option
import org.eureka.kotlin.fp.ch4.OptionOf
import org.eureka.kotlin.fp.ch4.fix
import org.eureka.kotlin.fp.ch4.flatMap
import org.eureka.kotlin.fp.ch7.ForPar
import org.eureka.kotlin.fp.ch7.ParOf
import org.eureka.kotlin.fp.ch7.Pars
import org.eureka.kotlin.fp.ch7.fix

interface Monad<F> : Functor<F> {
    fun <A> unit(a: A): Kind<F, A>
    fun <A, B> flatMap(fa: Kind<F, A>, f: (A) -> Kind<F, B>): Kind<F, B>

    override fun <A, B> map(
        fa: Kind<F, A>,
        f: (A) -> B
    ): Kind<F, B> = flatMap(fa) { a -> unit(f(a)) }

    fun <A, B, C> map2(
        fa: Kind<F, A>,
        fb: Kind<F, B>,
        f: (A, B) -> C
    ): Kind<F, C> = flatMap(fa) { a -> map(fb) { b -> f(a, b) } }

    fun <A> sequence(lfa: List<Kind<F, A>>): Kind<F, List<A>> = traverse(lfa) { it }

    fun <A, B> traverse(
        la: List<A>,
        f: (A) -> Kind<F, B>
    ): Kind<F, List<B>> =
        List.foldRight(la, unit(List.empty())) { el, fAcc -> map2(f(el), fAcc) { fEl, acc -> List.cons(fEl, acc) } }

    fun <A> replicateM(n: Int, ma: Kind<F, A>): Kind<F, List<A>> =
        map(ma) { a ->
            var l = List.empty<A>()
            repeat(n) {
                l = List.cons(a, l)
            }
            l
        }

    fun <A, B> product(
        ma: Kind<F, A>,
        mb: Kind<F, B>
    ): Kind<F, Pair<A, B>> =
        map2(ma, mb) { a, b -> a to b }

    fun <A> filterM(
        ms: List<A>,
        f: (A) -> Kind<F, Boolean>
    ): Kind<F, List<A>> =
        when (ms) {
            is Nil -> unit(Nil)
            is Cons ->
                flatMap(f(ms.head)) { succeed ->
                    if (succeed) {
                        map(filterM(ms.tail, f)) { tail ->
                            Cons(ms.head, tail)
                        }
                    } else filterM(ms.tail, f)
                }
        }

}

object MonadInstances {

    fun listMonad(): Monad<ForList> {
        return object : Monad<ForList> {
            override fun <A> unit(a: A): ListOf<A> = List.of(a)
            override fun <A, B> flatMap(fa: ListOf<A>, f: (A) -> ListOf<B>): ListOf<B> =
                List.flatMap(fa.fix()) { a -> f(a).fix() }
        }
    }

    fun parMonad(): Monad<ForPar> {
        return object : Monad<ForPar> {
            override fun <A> unit(a: A): ParOf<A> = Pars.unit(a)
            override fun <A, B> flatMap(fa: ParOf<A>, f: (A) -> ParOf<B>): ParOf<B> =
                Pars.flatMap(fa.fix()) { a -> f(a).fix() }
        }
    }

    fun optionMonad(): Monad<ForOption> {
        return object : Monad<ForOption> {
            override fun <A> unit(a: A): OptionOf<A> = Option.of(a)
            override fun <A, B> flatMap(fa: OptionOf<A>, f: (A) -> OptionOf<B>): OptionOf<B> =
                fa.fix().flatMap { a -> f(a).fix() }
        }
    }

    fun listKMonad(): Monad<ForListK> {
        return object : Monad<ForListK> {
            override fun <A> unit(a: A): Kind<ForListK, A> = listOf(a).k()
            override fun <A, B> flatMap(fa: Kind<ForListK, A>, f: (A) -> Kind<ForListK, B>): Kind<ForListK, B> =
                fa.fix().flatMap { a -> f(a).fix() }
        }
    }

    fun sequenceKMonad(): Monad<ForSequenceK> {
        return object : Monad<ForSequenceK> {
            override fun <A> unit(a: A): SequenceKOf<A> = sequenceOf(a).k()
            override fun <A, B> flatMap(fa: SequenceKOf<A>, f: (A) -> SequenceKOf<B>): SequenceKOf<B> =
                fa.fix().flatMap { a -> f(a) }
        }
    }

}