package org.eureka.kotlin.fp.ch9

import org.eureka.kotlin.fp.ch8.Checkers
import org.eureka.kotlin.fp.ch8.Gen
import org.eureka.kotlin.fp.ch8.Prop

abstract class ParserLaws : Parsers<ParseError> {
    private fun <A> equal(p1: Parser<A>, p2: Parser<A>, i: Gen<String>): Prop =
        Checkers.forAll(i) { s -> run(p1, s) == run(p2, s) }

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

    fun <A, B, C> unbiasL(p: Pair<Pair<A, B>, C>): Triple<A, B, C> =
        Triple(p.first.first, p.first.second, p.second)

    fun <A, B, C> unbiasR(p: Pair<A, Pair<B, C>>): Triple<A, B, C> =
        Triple(p.first, p.second.first, p.second.second)

}