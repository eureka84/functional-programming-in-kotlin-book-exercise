package org.eureka.kotlin.fp.ch7

import arrow.syntax.function.curried
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

typealias Par<A> = (ExecutorService) -> Future<A>

object Pars {

    fun <A> run(es: ExecutorService, a: Par<A>): Future<A> = a(es)
    fun <A, B> asyncF(f: (A) -> B): (A) -> Par<B> = { a -> lazyUnit { f(a) } }

    fun <A> lazyUnit(a: () -> A): Par<A> =
        fork { unit(a()) }

    fun <A> unit(a: A): Par<A> =
        { _: ExecutorService -> UnitFuture(a) }

    data class UnitFuture<A>(val a: A) : Future<A> {
        override fun get(): A = a
        override fun get(timeout: Long, timeUnit: TimeUnit): A = a
        override fun cancel(evenIfRunning: Boolean): Boolean = false
        override fun isDone(): Boolean = true
        override fun isCancelled(): Boolean = false
    }

    data class TimedMap2Future<A, B, C>(
        val pa: Future<A>,
        val pb: Future<B>,
        val f: (A, B) -> C
    ) : Future<C> {

        override fun isDone(): Boolean = pa.isDone && pb.isDone
        override fun get(): C {
            return f(pa.get(), pb.get())
        }

        override fun get(to: Long, tu: TimeUnit): C {
            val timeoutMillis = TimeUnit.MILLISECONDS.convert(to, tu)
            val start = System.currentTimeMillis()
            val a = pa.get(to, tu)
            val duration = System.currentTimeMillis() - start
            val remainder = timeoutMillis - duration
            val b = pb.get(remainder, TimeUnit.MILLISECONDS)
            return f(a, b)
        }

        override fun cancel(mayInterruptIfRunning: Boolean): Boolean =
            pa.cancel(mayInterruptIfRunning) && pb.cancel(mayInterruptIfRunning)

        override fun isCancelled(): Boolean =
            pa.isCancelled && pb.isCancelled
    }

    fun <A, B, C> map2(
        a: Par<A>,
        b: Par<B>,
        f: (A, B) -> C
    ): Par<C> =
        { es: ExecutorService ->
            val af: Future<A> = a(es)
            val bf: Future<B> = b(es)
            TimedMap2Future(af, bf, f)
        }

    fun <A, B, C, D> map3(
        parA: Par<A>,
        parB: Par<B>,
        parC: Par<C>,
        f: (A, B, C) -> D
    ): Par<D> =
        map2(
            parC,
            map2(
                parA,
                parB
            ) { a, b -> f.curried()(a)(b) }
        ) { c, g -> g(c) }

    fun <A, B, C, D, E> map4(
        parA: Par<A>,
        parB: Par<B>,
        parC: Par<C>,
        parD: Par<D>,
        f: (A, B, C, D) -> E
    ): Par<E> =
        map2(
            parD,
            map2(
                parC,
                map2(
                    parA,
                    parB,
                    { a, b -> f.curried()(a)(b) }
                ),
                { c, g -> g(c) }
            ),
            { d: D, h -> h(d) }
        )

    fun <A, B, C, D, E, F> map5(
        parA: Par<A>,
        parB: Par<B>,
        parC: Par<C>,
        parD: Par<D>,
        parE: Par<E>,
        f: (A, B, C, D, E) -> F
    ): Par<F> =
        map2(
            parE,
            map2(
                parD,
                map2(
                    parC,
                    map2(
                        parA,
                        parB,
                        { a: A, b: B -> f.curried()(a)(b) }
                    ),
                    { c, g -> g(c) }
                ),
                { d: D, h -> h(d) }
            )
        ) { e: E, i -> i(e) }


    fun <A> fork(a: () -> Par<A>): Par<A> =
        { es: ExecutorService ->
            es.submit(Callable {
                val valA: Par<A> = a()
                valA(es).get()
            })
        }

    fun sortPar(parList: Par<List<Int>>): Par<List<Int>> =
        map(parList) { it.sorted() }

    fun <A, B> map(pa: Par<A>, f: (A) -> B): Par<B> =
        map2(pa, unit(Unit), { a, _ -> f(a) })

    fun <A, B> parMap(
        ps: List<A>,
        f: (A) -> B
    ): Par<List<B>> = fork {
        val fbs: List<Par<B>> = ps.map(asyncF(f))
        sequence(fbs)
    }

    fun <A> sequence(ps: List<Par<A>>): Par<List<A>> =
        when {
            ps.isEmpty() -> unit(Nil)
            ps.size == 1 -> map(ps.head) { listOf(it) }
            else -> {
                val (l, r) = ps.splitAt(ps.size / 2)
                map2(
                    fork { sequence(l) },
                    fork { sequence(r) }
                ) { la, lb -> la + lb }
            }
        }

    fun <A> parFilter(
        sa: List<A>,
        f: (A) -> Boolean
    ): Par<List<A>> = when {
        sa.isEmpty() -> unit(Nil)
        sa.size == 1 -> if (f(sa.head)) unit(sa) else unit(Nil)
        else -> {
            val (l, r) = sa.splitAt(sa.size / 2)
            map2(
                fork { parFilter(l, f) },
                fork { parFilter(r, f) }
            ) { la, lb -> la + lb }
        }
    }

    fun <A> choiceN(n: Par<Int>, choices: List<Par<A>>): Par<A> = { es ->
        run(es, choices[run(es, n).get()])
    }

    fun <A> choice(cond: Par<Boolean>, t: Par<A>, f: Par<A>): Par<A> =
        choiceN(map(cond) {b -> if (b) 0 else 1 }, listOf(t, f))

    fun <K, V> choiceMap(
        key: Par<K>,
        choices: Map<K, Par<V>>
    ): Par<V> = { es -> run(es, choices.getValue(run(es, key).get())) }

    fun <A, B> flatMap(pa: Par<A>, choices: (A) -> Par<B>): Par<B> = { es ->
        run(es, choices(run(es, pa).get()))
    }

    fun <A> join(a: Par<Par<A>>): Par<A> = flatMap(a) { it }

}

val <T> List<T>.head: T
    get() = first()
val <T> List<T>.tail: List<T>
    get() = this.drop(1)
val Nil = listOf<Nothing>()


