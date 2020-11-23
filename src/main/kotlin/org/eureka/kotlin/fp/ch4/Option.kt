package org.eureka.kotlin.fp.ch4

sealed class Option<out A> {
    companion object {
        fun <A> of(a: A): Option<A> = Some(a)
        fun <A> empty(): Option<A> = None
        fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = { oa -> oa.map(f) }
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
