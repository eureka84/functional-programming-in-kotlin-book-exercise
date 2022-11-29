package org.eureka.kotlin.fp.ch12

import arrow.Kind
import arrow.Kind2

class ForValidation
typealias ValidationPartialOf<E> = Kind<ForValidation, E>
typealias ValidationOf<E, A> = Kind2<ForValidation, E, A>

fun <E, A> ValidationOf<E, A>.fix(): Validation<E, A> = this as Validation<E, A>

sealed class Validation<out E, out A> : ValidationOf<E, A> {

    companion object {
        fun <E> applicative(): Applicative<ValidationPartialOf<E>> = object : Applicative<ValidationPartialOf<E>> {
            override fun <A> unit(a: A): ValidationOf<E, A> = Success(a)

            override fun <A, B> apply(
                fab: ValidationOf<E, (A) -> B>,
                fa: ValidationOf<E, A>
            ): ValidationOf<E, B> = when (val func = fab.fix()) {
                is Failure -> when (val value = fa.fix()) {
                    is Failure -> func.copy(tail = func.tail + listOf(value.head) + value.tail)
                    is Success -> func
                }
                is Success -> when (val value = fa.fix()) {
                    is Failure -> value
                    is Success -> Success(func.a(value.a))
                }
            }

        }
    }

}

data class Failure<E>(val head: E, val tail: List<E> = emptyList()) : Validation<E, Nothing>()

data class Success<A>(val a: A) : Validation<Nothing, A>()