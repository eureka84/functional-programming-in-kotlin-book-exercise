package org.eureka.kotlin.fp.ch2

object Recursion {

    fun fibonacci(n: Int): Int {
        tailrec fun loop(n: Int, last: Int, secondToLast: Int): Int =
            if (n == 0)
                last
            else
                loop(n - 1, last + secondToLast, last)

        return loop(n, 1, 0)
    }

}
