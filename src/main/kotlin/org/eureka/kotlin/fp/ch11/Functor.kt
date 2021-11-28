package org.eureka.kotlin.fp.ch11

import arrow.Kind

interface Functor<F> {
    fun <A, B> map(fa: Kind<F, A>, f: (A) -> B): Kind<F, B>
}