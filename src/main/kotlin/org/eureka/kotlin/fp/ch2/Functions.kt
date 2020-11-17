package org.eureka.kotlin.fp.ch2

object Functions {

    fun <A, B, C> partial(a: A, f: (A, B) -> C): (B) -> C = { b -> f(a, b) }

    fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b -> f(a, b) } }

    fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C = { a, b -> f(a)(b) }

    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { a -> f(g(a)) }

}