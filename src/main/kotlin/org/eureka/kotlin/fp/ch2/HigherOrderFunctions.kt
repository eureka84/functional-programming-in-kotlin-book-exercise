package org.eureka.kotlin.fp.ch2

object HigherOrderFunctions {

    private val <T> List<T>.tail: List<T>
        get() = drop(1)

    private val <T> List<T>.head: T
        get() = first()

    fun <A> isSorted(aa: List<A>, order: (A, A) -> Boolean): Boolean {
        tailrec fun loop (a: A, b: A, remainder: List<A>): Boolean =
            when {
                remainder.isEmpty() -> order(a,b)
                order(a,b)          -> loop(b, remainder.head, remainder.tail)
                else                -> false
            }

        return (aa.isEmpty() || aa.size == 1) || loop(aa.head, aa.tail.head, aa.tail.tail)
    }

}