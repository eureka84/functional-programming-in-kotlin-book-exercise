package org.eureka.kotlin.fp.ch2

typealias Function<A, B> = (A) -> B

infix fun <A, B, C> Function<A, B>.andThen(g: Function<B, C>): Function<A, C> = { a: A -> g(this(a)) }

object Functions {

    fun <A, B, C> partial(a: A, f: (A, B) -> C): (B) -> C = { b -> f(a, b) }

    fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b -> f(a, b) } }
    fun <A, B, C, D> curry(f: (A, B, C) -> D): (A) -> (B) -> (C) -> D = { a -> { b -> {c -> f(a, b, c) } } }

    fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C = { a, b -> f(a)(b) }

    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { a -> f(g(a)) }

}