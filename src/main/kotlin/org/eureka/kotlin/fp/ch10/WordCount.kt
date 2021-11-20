package org.eureka.kotlin.fp.ch10

import org.eureka.kotlin.fp.ch10.MonoidExtensions.foldMap
import kotlin.math.min

sealed class WC {
    abstract operator fun plus(other: WC): WC
}

data class Stub(val chars: String) : WC() {
    override operator fun plus(other: WC) = when (other) {
        is Stub -> Stub(this.chars + other.chars)
        is Part -> other.copy(ls = this.chars + other.ls)
    }
}

data class Part(val ls: String, val words: Int, val rs: String) : WC() {
    override fun plus(other: WC): WC = when (other) {
        is Stub -> this.copy(rs = this.rs + other.chars)
        is Part -> Part(
            this.ls,
            this.words + other.words + (if ((this.rs + other.ls).isEmpty()) 0 else 1),
            other.rs
        )
    }
}

fun wcMonoid(): Monoid<WC> = object : Monoid<WC> {
    override fun combine(a1: WC, a2: WC): WC = a1 + a2
    override val nil: WC = Stub("")
}

fun wordCount(s: String): Int {
    fun parseChar(c: Char): WC =
        if (c.isWhitespace()) Part("", 0, "")
        else Stub("$c")
    fun unStub(s: String): Int = min(s.length, 1)
    return when (val wc = foldMap(s.toList(), wcMonoid()) { parseChar(it) }) {
        is Stub -> unStub(wc.chars)
        is Part -> unStub(wc.rs) + wc.words + unStub(wc.rs)
    }
}

