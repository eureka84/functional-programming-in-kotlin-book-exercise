package org.eureka.kotlin.fp.ch7

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
        ps.fold(unit(listOf())) { acc, par -> map2(acc, par) { l, a -> l + a } }

}


