package org.eureka.kotlin.fp.ch7

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

typealias Par<A> = (ExecutorService) -> Future<A>

fun <A> run(es: ExecutorService, a: Par<A>): Future<A> = a(es)

object Pars {

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

    fun <A, B, C> map2(
        a: Par<A>,
        b: Par<B>,
        f: (A, B) -> C
    ): Par<C> =
        { es: ExecutorService ->
            val af: Future<A> = a(es)
            val bf: Future<B> = b(es)
            UnitFuture(f(af.get(), bf.get()))
        }

    fun <A> fork(a: () -> Par<A>): Par<A> =
        { es: ExecutorService ->
            es.submit(Callable { a()(es).get() })
        }
}


