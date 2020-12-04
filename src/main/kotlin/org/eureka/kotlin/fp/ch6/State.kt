package org.eureka.kotlin.fp.ch6

import org.eureka.kotlin.fp.ch3.List

data class State<S, out A>(val run: (S) -> Pair<A, S>) {

    fun <B> map(f: (A) -> B): State<S, B> = flatMap { a ->
        State { s -> Pair(f(a), s) }
    }

    fun <B> flatMap(f: (A) -> State<S, B>): State<S, B> =
        State { s ->
            val (a, s1) = this.run(s)
            f(a).run(s1)
        }


    companion object {
        fun <S, A> unit(a: A): State<S, A> = State { s -> Pair(a, s) }

        fun <S, A, B, C> map2(sa: State<S, A>, sb: State<S, B>, f: (A, B) -> C): State<S, C> =
            sa.flatMap { a ->
                sb.map { b -> f(a, b) }
            }

        fun <S, A> sequence(ls: List<State<S, A>>): State<S, List<A>> =
            List.foldLeft(ls, unit(List.empty())) { acc, r ->
                map2(acc, r) { l, a -> List.cons(a, l) }
            }
    }

}