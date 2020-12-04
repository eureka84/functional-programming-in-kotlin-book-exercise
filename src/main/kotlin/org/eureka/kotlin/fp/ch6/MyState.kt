package org.eureka.kotlin.fp.ch6

import org.eureka.kotlin.fp.ch3.List

data class MyState<S, out A>(val run: (S) -> Pair<A, S>) {

    fun <B> map(f: (A) -> B): MyState<S, B> = flatMap { a ->
        MyState { s -> Pair(f(a), s) }
    }

    fun <B> flatMap(f: (A) -> MyState<S, B>): MyState<S, B> =
        MyState { s ->
            val (a, s1) = this.run(s)
            f(a).run(s1)
        }


    companion object {
        fun <S, A> unit(a: A): MyState<S, A> = MyState { s -> Pair(a, s) }

        fun <S, A, B, C> map2(sa: MyState<S, A>, sb: MyState<S, B>, f: (A, B) -> C): MyState<S, C> =
            sa.flatMap { a ->
                sb.map { b -> f(a, b) }
            }

        fun <S, A> sequence(ls: List<MyState<S, A>>): MyState<S, List<A>> =
            List.foldLeft(ls, unit(List.empty())) { acc, r ->
                map2(acc, r) { l, a -> List.cons(a, l) }
            }
    }

}