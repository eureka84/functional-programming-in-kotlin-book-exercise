package org.eureka.kotlin.fp.ch5

import org.eureka.kotlin.fp.ch4.Option
import org.eureka.kotlin.fp.ch5.Stream.Companion.unfold

object InfiniteStreams {

    fun ones(): Stream<Int> = constant(1)

    fun <A> constant(a: A): Stream<A> =
        unfold(a) { Option.of(Pair(a, a))}
//        Stream.cons({ a }, { constant(a) })

    fun from(n: Int): Stream<Int> =
        unfold(n) { s -> Option.of(Pair(s, s + 1)) }
//        Stream.cons({ n }, { from(n + 1) })

    fun fibs(): Stream<Int> {
        data class LastTwo(val secondToLast: Int, val last: Int) {
            fun move(): LastTwo = LastTwo(last, secondToLast + last)
        }

        return unfold(LastTwo(0, 1)) { lastTwo ->
            Option.of(
                Pair(
                    lastTwo.secondToLast,
                    lastTwo.move()
                )
            )
        }
    }

}