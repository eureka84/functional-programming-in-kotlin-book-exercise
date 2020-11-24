package org.eureka.kotlin.fp.ch4

import org.eureka.kotlin.fp.ch3.Cons
import org.eureka.kotlin.fp.ch3.List
import org.eureka.kotlin.fp.ch4.Invalid.Companion.combine
import org.eureka.kotlin.fp.ch4.Validated.Companion.valid

sealed class Validated<out E, out A> {
    companion object {
        fun <E, A> valid(a: A): Validated<E, A> = Valid(a)
        fun <E, A> invalid(e: E): Validated<E, A> = Invalid(e)
        fun <E, A, B, C> map2(v1: Validated<E, A>, v2: Validated<E, B>, f: (A, B) -> C): Validated<E, C> =
            when(v1) {
                is Invalid ->
                    when (v2) {
                        is Invalid -> combine(v1, v2)
                        is Valid -> v1
                    }
                is Valid ->
                    when(v2) {
                        is Invalid -> v2
                        is Valid -> Valid(f(v1.value, v2.value))
                    }
            }
        fun <E, A> sequence(xs: List<Validated<E,A>>): Validated<E, List<A>> =
            traverse(xs) { it }

        fun <E, A, B> traverse(
            xa: List<A>,
            f: (A) -> Validated<E, B>
        ): Validated<E, List<B>> = List.foldRight(xa, valid(List.empty())) { a, acc ->
            map2(acc, f(a)) { list, b -> Cons(b, list) }
        }
    }
}
data class Valid<out A>(val value: A) : Validated<Nothing, A>()
data class Invalid<out E>(val errors: List<E>) : Validated<E, Nothing>() {
    constructor(vararg e: E): this(List.of(*e))

    companion object {
        fun <E> combine(i1: Invalid<E>, i2: Invalid<E>): Invalid<E> =
            Invalid(List.append(i1.errors, i2.errors))
    }
}

fun <E, A, B> Validated<E, A>.map(f: (A) -> B): Validated<E, B> = when(this) {
    is Invalid -> this
    is Valid -> valid(f(this.value))
}
