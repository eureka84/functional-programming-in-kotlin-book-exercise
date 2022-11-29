package org.eureka.kotlin.fp.ch12

import arrow.Kind
import org.eureka.kotlin.fp.ch11.Functor
import org.eureka.kotlin.fp.ch3.ForList
import org.eureka.kotlin.fp.ch3.ListOf
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch3.fix
import org.eureka.kotlin.fp.ch4.*

interface Traversable<F>: Functor<F> {

    fun <G, A, B> traverse(fa: Kind<F, A>, AG: Applicative<G>, f: (A) -> Kind<G, B>): Kind<G, Kind<F, B>> =
        sequence(map(fa, f), AG)

    fun <G, A> sequence(fa: Kind<F, Kind<G,A>>, AG: Applicative<G>): Kind<G, Kind<F, A>>
}

object TraversableInstances {

    fun <A> optionTraversable(): Traversable<ForOption> = object : Traversable<ForOption> {
        override fun <A, B> map(fa: Kind<ForOption, A>, f: (A) -> B): Kind<ForOption, B> {
            return fa.fix().map(f)
        }

        override fun <G, A> sequence(fa: OptionOf<Kind<G, A>>, AG: Applicative<G>): Kind<G, OptionOf<A>> {
            val default = AG.unit(Option.empty<A>())
            return fa.fix().fold(default) { ga: Kind<G, A> ->
                AG.map2(ga, default) { a, _ -> Option.some(a)}
            }
        }
    }

    fun listTraversable(): Traversable<ForList> = object : Traversable<ForList> {
        override fun <A, B> map(fa: ListOf<A>, f: (A) -> B): ListOf<B> {
            return List.map(fa.fix(), f)
        }

        override fun <G, A> sequence(fa: ListOf<Kind<G, A>>, AG: Applicative<G>): Kind<G, ListOf<A>> {
            return List.foldRight(fa.fix(), AG.unit(List.empty())) { ga: Kind<G, A>, acc: Kind<G, ListOf<A>> ->
                AG.map2(ga, acc) { a, list -> List.cons(a, list.fix())}
            }
        }

    }

}