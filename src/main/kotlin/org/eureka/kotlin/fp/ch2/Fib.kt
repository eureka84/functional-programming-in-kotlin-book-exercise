package org.eureka.kotlin.fp.ch2

object Fib {

    fun fibonacci(n: Int): Int {
        tailrec fun loop(n: Int, prev:Int, secondToLast: Int): Int =
            if (n == 0)
                prev
            else
                loop(n-1, prev+secondToLast, prev)
        return loop(n,1, 0)
    }

}
