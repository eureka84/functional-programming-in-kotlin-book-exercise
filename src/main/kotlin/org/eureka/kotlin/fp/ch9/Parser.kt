package org.eureka.kotlin.fp.ch9

import arrow.core.Either
import org.eureka.kotlin.fp.ch8.Checkers.forAll
import org.eureka.kotlin.fp.ch8.Gen
import org.eureka.kotlin.fp.ch8.Prop
import org.eureka.kotlin.fp.ch9.Parsers.Parser

interface Parsers<PE> {

    interface Parser<A>

    fun string(s: String): Parser<String>

    fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B>
    fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B>

    infix fun <A, B> Parser<A>.product(pb: () -> Parser<B>): Parser<Pair<A, B>>

    fun <A, B, C> map2(
        pa: Parser<A>,
        pb: () -> Parser<B>,
        f: (A, B) -> C
    ): Parser<C> = pa.product(pb).map { (a, b) -> f(a, b) }

    fun <A> succeed(a: A): Parser<A> = string("").map { a }

    fun char(c: Char): Parser<Char> = string(c.toString()).map { it[0] }

    infix fun <A> Parser<A>.or(pb: () -> Parser<A>): Parser<A>

    fun <A> listOfN(n: Int, p: Parser<A>): Parser<List<A>> =
        if (n > 0) {
            map2(p, { listOfN(n - 1, p) }) { a, la -> listOf(a) + la }
        } else {
            succeed(emptyList())
        }

    fun <A> Parser<A>.many(): Parser<List<A>> =
        map2(this, { this.many() }) { a, l -> listOf(a) + l } or { succeed(emptyList()) }

    fun <A> Parser<A>.slice(): Parser<String>

    fun <A> many1(p: Parser<A>): Parser<List<A>> = map2(p, { p.many() }) { a, la -> listOf(a) + la }

    infix fun String.or(other: String): Parser<String> =
        string(this) or { string(other) }

    fun <A, B, C> unbiasL(p: Pair<Pair<A, B>, C>): Triple<A, B, C> =
        Triple(p.first.first, p.first.second, p.second)

    fun <A, B, C> unbiasR(p: Pair<A, Pair<B, C>>): Triple<A, B, C> =
        Triple(p.first, p.second.first, p.second.second)

    fun <A> run(p: Parser<A>, input: String): Either<PE, A>
}

abstract class Examples : Parsers<ParseError> {

    private fun regex(s: String): Parser<String> = TODO()

    val parser: Parser<Int> = regex("[0-9]+").flatMap { digit: String ->
        val n = digit.toInt()
        listOfN(n, char('a')).map { n }
    }

}

object ParseError

abstract class Laws : Parsers<ParseError> {
    private fun <A> equal(p1: Parser<A>, p2: Parser<A>, i: Gen<String>): Prop =
        forAll(i) { s -> run(p1, s) == run(p2, s) }

    fun <A> mapLaw(p: Parser<A>, i: Gen<String>): Prop =
        equal(p, p.map { a -> a }, i)

    // associativity
    fun <A, B, C> productAssociativity(a: Parser<A>, b: Parser<B>, c: Parser<C>, i: Gen<String>) =
        equal(
            ((a product { b }) product { c }).map(::unbiasL),
            (a product { b product { c } }).map(::unbiasR),
            i
        )

    fun <A, B, C, D> mapAndProduct(a: Parser<A>, b: Parser<B>, f: (A) -> C, g: (B) -> D, i: Gen<String>) =
        equal(
            a.map(f) product { b.map(g) },
            (a product { b }).map { (a1, b1) -> Pair(f(a1), g(b1)) },
            i
        )
}