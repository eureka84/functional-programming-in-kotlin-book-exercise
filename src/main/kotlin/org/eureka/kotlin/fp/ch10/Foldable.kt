package org.eureka.kotlin.fp.ch10

import arrow.Kind
import arrow.core.ForListK
import arrow.core.ForOption
import arrow.core.ListKOf
import arrow.core.fix
import org.eureka.kotlin.fp.ch10.MonoidInstances.dual
import org.eureka.kotlin.fp.ch10.MonoidInstances.endoMonoid
import org.eureka.kotlin.fp.ch3.ForTree
import org.eureka.kotlin.fp.ch3.Tree
import org.eureka.kotlin.fp.ch3.fix

interface Foldable<F> {
    fun <A, B> foldRight(fa: Kind<F, A>, z: B, f: (A, B) -> B): B =
        foldMap(fa, endoMonoid()) { a: A -> { b: B -> f(a, b) } }(z)

    fun <A, B> foldLeft(fa: Kind<F, A>, z: B, f: (B, A) -> B): B =
        foldMap(fa, dual(endoMonoid())) { a: A -> { b: B -> f(b, a) } }(z)

    fun <A, B> foldMap(fa: Kind<F, A>, m: Monoid<B>, f: (A) -> B): B =
        foldRight(fa, m.nil) { a, b -> m.combine(f(a), b) }

    fun <A> toList(fa: Kind<F, A>): List<A> = foldLeft(fa, listOf()) { l, a -> l + a }
}

object ListFoldable : Foldable<ForListK> {
    override fun <A, B> foldRight(
        fa: ListKOf<A>,
        z: B,
        f: (A, B) -> B
    ): B =
        fa.fix().foldRight(z, f)

    override fun <A, B> foldLeft(
        fa: ListKOf<A>,
        z: B,
        f: (B, A) -> B
    ): B =
        fa.fix().foldLeft(z, f)
}

object TreeFoldable : Foldable<ForTree> {
    override fun <A, B> foldMap(fa: Kind<ForTree, A>, m: Monoid<B>, f: (A) -> B): B {
        return Tree.fold(fa.fix(), f, m::combine)
    }
}

object OptionFoldable : Foldable<ForOption> {
    override fun <A, B> foldMap(fa: Kind<ForOption, A>, m: Monoid<B>, f: (A) -> B): B {
        return fa.fix().fold({ m.nil }, f)
    }
}

