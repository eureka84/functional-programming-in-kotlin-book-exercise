package org.eureka.kotlin.fp.ch9

import arrow.core.Either

typealias Parser<A> = (Location) -> Result<A>
sealed class Result<out A>
data class Success<out A>(val a: A, val consumed: Int) : Result<A>()
data class Failure(val parserError: ParseError, val committed: Boolean = true) : Result<Nothing>()

interface Parsers<PE> {

    fun <A> succeed(a: A): Parser<A> = string("").map { a }
    fun <A, B> Parser<A>.flatMap(f: (A) -> Parser<B>): Parser<B>

    fun string(s: String): Parser<String>

    fun <A, B> Parser<A>.map(f: (A) -> B): Parser<B> = this.flatMap { a -> succeed(f(a)) }

    infix fun <A, B> Parser<A>.product(pb: () -> Parser<B>): Parser<Pair<A, B>> =
        this.flatMap { a -> pb().map { b -> Pair(a,b) } }

    fun <A, B, C> map2(
        pa: Parser<A>,
        pb: () -> Parser<B>,
        f: (A, B) -> C
    ): Parser<C> =  pa.flatMap { a -> pb().map { b -> f(a,b) } }

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

    fun <A> defer(pa: Parser<A>): () -> Parser<A>

    infix fun <A> Parser<A>.skipR(ps: Parser<String>): Parser<A>
    infix fun <B> Parser<String>.skipL(pb: Parser<B>): Parser<B>
    infix fun <A> Parser<A>.sep(p2: Parser<String>): Parser<List<A>>
    fun <A> surround(start: Parser<String>, stop: Parser<String>, p: Parser<A>): Parser<A>

    fun <A> run(p: Parser<A>, input: String): Either<PE, A>

    fun regex(s: String): Parser<String>

    fun <A> tag(msg: String, p: Parser<A>): Parser<A>
    fun <A> scope(msg: String, p: Parser<A>): Parser<A>
    fun <A> attempt(p: Parser<A>): Parser<A>

}

data class ParseError(val stack: List<Pair<Location, String>>)

data class Location(val input: String, val offset: Int = 0) {
    private val slice by lazy { input.slice(0..offset + 1) }
    val line by lazy { slice.count { it == '\n' } + 1 }
    val column by lazy {
        when (val n = slice.lastIndexOf('\n')) {
            -1 -> offset + 1
            else -> offset - n
        }
    }
}
fun errorLocation(e: ParseError): Location = TODO()
fun errorMessage(e: ParseError): String = TODO()
