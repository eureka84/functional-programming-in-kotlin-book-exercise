package org.eureka.kotlin.fp.ch4

import arrow.Kind
import org.eureka.kotlin.fp.ch3.Cons
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch3.List.Companion.foldRight
import java.lang.Exception

class ForOption
typealias OptionOf<A> = Kind<ForOption, A>
fun <A> OptionOf<A>.fix(): Option<A> = this as Option<A>

sealed class Option<out A>: OptionOf<A> {

    fun <B> fold(default: B, f: (A) -> B): B = this.map(f).getOrElse { default }

    companion object {
        fun <A> some(a: A): Option<A> = Some(a)
        fun <A> none(): Option<A> = None
        fun <A> of(a: A): Option<A> = Some(a)
        fun <A> empty(): Option<A> = None
        fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = { oa -> oa.map(f) }
        fun <A, B, C> map2(a: Option<A>, b: Option<B>, f: (A, B) -> C): Option<C> =
            a.flatMap { aVal -> b.map { bVal -> f(aVal, bVal) } }

        fun <A> sequence(xs: List<Option<A>>): Option<List<A>> =
            traverse(xs) { it }

        fun <A, B> traverse(
            xa: List<A>,
            f: (A) -> Option<B>
        ): Option<List<B>> = foldRight(xa, of(List.empty())) { a, acc -> map2(acc, f(a)) { list, b -> Cons(b, list) } }

        fun <A> catches(supplier: ()->A): Option<A> =
            try {
                of(supplier())
            } catch (e: Exception) {
                empty()
            }
    }
}

data class Some<out A>(val get: A) : Option<A>()
object None : Option<Nothing>()

fun <A, B> Option<A>.map(f: (A) -> B): Option<B> = when (this) {
    is Some -> Some(f(this.get))
    is None -> None
}

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> = this.map(f).getOrElse { None }

fun <A> Option<A>.getOrElse(default: () -> A): A = when (this) {
    is Some -> this.get
    is None -> default()
}

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = this.map { a -> Some(a) }.getOrElse(ob)

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> = this.flatMap { a -> if (f(a)) Some(a) else None }

fun <A> Option<A>.isEmpty(): Boolean =
    when(this) {
        is Some -> false
        is None -> true
    }
