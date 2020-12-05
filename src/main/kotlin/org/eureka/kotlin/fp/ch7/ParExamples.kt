package org.eureka.kotlin.fp.ch7

import arrow.core.extensions.list.foldable.firstOption
import arrow.core.getOrElse
import org.eureka.kotlin.fp.ch7.Pars.fork
import org.eureka.kotlin.fp.ch7.Pars.map2
import org.eureka.kotlin.fp.ch7.Pars.unit

object ParExamples {

    fun sum(ints: List<Int>): Par<Int> =
        if (ints.size <= 1)
            unit(ints.firstOption().getOrElse { 0 })
        else {
            val (l, r) = ints.chunked(ints.size / 2)
            map2(
                fork { sum(l) },
                fork { sum(r) }
            ) { lx: Int, rx: Int -> lx + rx }
        }


}