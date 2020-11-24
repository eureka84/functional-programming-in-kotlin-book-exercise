package org.eureka.kotlin.fp.ch4

import org.eureka.kotlin.fp.ch3.Cons
import org.eureka.kotlin.fp.ch3.List

sealed class Either<out E, out A> {
    companion object {
        fun <E, A> left(e: E): Either<E, A> = Left(e)
        fun <E, A> right(a: A): Either<E, A> = Right(a)

        fun <E, A, B, C> map2(
            ae: Either<E, A>,
            be: Either<E, B>,
            f: (A, B) -> C
        ): Either<E, C> = ae.flatMap { a -> be.map { b -> f(a, b) } }

        fun <A> catches(supplier: () -> A): Either<Exception, A> =
            try {
                right(supplier())
            } catch (e: Exception) {
                left(e)
            }

        fun <E, A> sequence(xs: List<Either<E,A>>): Either<E, List<A>> =
            traverse(xs) { it }

        fun <E, A, B> traverse(
            xa: List<A>,
            f: (A) -> Either<E, B>
        ): Either<E, List<B>> = List.foldRight(xa, right(List.empty())) { a, acc ->
            map2(acc, f(a)) { list, b -> Cons(b, list) }
        }
    }

}

data class Left<out E>(val value: E) : Either<E, Nothing>()
data class Right<out A>(val value: A) : Either<Nothing, A>()

fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> = when(this) {
    is Left -> this
    is Right -> Either.right(f(this.value))
}

fun <E, A, B> Either<E, A>.flatMap(f: (A) -> Either<E, B>): Either<E, B> = when(this) {
    is Left -> this
    is Right -> f(this.value)
}

fun <E, A> Either<E, A>.orElse(f: () -> Either<E, A>): Either<E, A> = when (this) {
    is Left -> f()
    is Right -> this
}

