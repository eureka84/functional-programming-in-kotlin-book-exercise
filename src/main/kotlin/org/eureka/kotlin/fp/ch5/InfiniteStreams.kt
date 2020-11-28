package org.eureka.kotlin.fp.ch5

import org.eureka.kotlin.fp.ch4.Option
import org.eureka.kotlin.fp.ch4.getOrElse
import org.eureka.kotlin.fp.ch4.map
import org.eureka.kotlin.fp.ch4.orElse

object InfiniteStreams {

    fun ones(): Stream<Int> = constant(1)

    fun <A> constant(a: A): Stream<A> = Stream.cons({ a }, { constant(a) })

    fun from(n: Int): Stream<Int> =
        unfold(n) { s -> Option.of(Pair(s, s + 1)) }
//        Stream.cons({ n }, { from(n + 1) })

    fun fibs(): Stream<Int> {
        fun loop(last: Int, secondToLast: Int): Stream<Int> =
            Stream.cons({ last + secondToLast }, { loop(last + secondToLast, last) })

        return Stream.cons({ 0 }, { Stream.cons({ 1 }, { loop(1, 0) }) })
    }

    fun <A, S> unfold(z: S, f: (S) -> Option<Pair<A, S>>): Stream<A> =
        f(z).map { (a: A, s: S) -> Stream.cons({ a }, { unfold(s, f) }) }.getOrElse { Stream.empty() }

}